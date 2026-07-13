package com.ferhatozcelik.jetpackcomposetemplate.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ferhatozcelik.jetpackcomposetemplate.data.dao.NearMissDao
import com.ferhatozcelik.jetpackcomposetemplate.data.entity.NearMissEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.ferhatozcelik.jetpackcomposetemplate.data.remote.api.PplApiService
import com.ferhatozcelik.jetpackcomposetemplate.data.remote.model.NearMissDto
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltViewModel
class NearMissViewModel @Inject constructor(
    private val nearMissDao: NearMissDao,
    private val apiService: PplApiService
) : ViewModel() {

    init {
        fetchRemoteNearMisses()
    }

    val allNearMisses: Flow<List<NearMissEntity>> = nearMissDao.getAllNearMisses()

    fun fetchRemoteNearMisses() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiService.getNearMisses()
                if (response.isSuccessful) {
                    response.body()?.let { dtos ->
                        nearMissDao.deleteAllNearMisses() // Clear local cache to prevent duplicates
                        dtos.forEach { dto ->
                            val entity = NearMissEntity(
                            title = dto.title,
                            description = dto.description,
                            plantArea = dto.plantArea,
                            type = dto.type,
                            criticality = dto.criticality.toIntOrNull() ?: 1,
                            probability = dto.probability.toIntOrNull() ?: 1,
                            riskScore = dto.riskScore,
                            photoUrl = dto.photoUrl,
                            resolved = dto.resolved
                        )
                        // Ignore conflicts or just insert
                        nearMissDao.insertNearMiss(entity)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun insertNearMiss(
        title: String,
        description: String,
        plantArea: String,
        type: String,
        criticality: Int,
        probability: Int,
        photoBitmap: android.graphics.Bitmap? = null
    ) {
        val riskScore = criticality * probability
        val entity = NearMissEntity(
            title = title,
            description = description,
            plantArea = plantArea,
            type = type,
            criticality = criticality,
            probability = probability,
            riskScore = riskScore
        )
        
        viewModelScope.launch(Dispatchers.IO) {
            // 1. Save locally
            nearMissDao.insertNearMiss(entity)
            
            // 2. Push to backend
            try {
                val dto = NearMissDto(
                    title = title,
                    description = description,
                    plantArea = plantArea,
                    type = type,
                    criticality = criticality.toString(),
                    probability = probability.toString(),
                    riskScore = riskScore,
                    timestamp = System.currentTimeMillis().toString(),
                    photoUrl = null,
                    resolved = false
                )
                val json = Gson().toJson(dto)
                val body = json.toRequestBody("application/json".toMediaTypeOrNull())

                var photoPart: okhttp3.MultipartBody.Part? = null
                photoBitmap?.let { bitmap ->
                    val stream = java.io.ByteArrayOutputStream()
                    bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 90, stream)
                    val byteArray = stream.toByteArray()
                    val requestBody = byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull(), 0, byteArray.size)
                    photoPart = okhttp3.MultipartBody.Part.createFormData("photo", "photo.jpg", requestBody)
                }

                apiService.submitNearMiss(body, photoPart)
                
                // Fetch the updated list from server so the UI gets the correct photoUrl
                fetchRemoteNearMisses()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun resolveNearMiss(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Optimistically update local UI
                val current = nearMissDao.getNearMissById(id)
                if (current != null) {
                    nearMissDao.insertNearMiss(current.copy(resolved = true))
                }
                
                // Call backend
                apiService.resolveNearMiss(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
