package com.ferhatozcelik.jetpackcomposetemplate.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ferhatozcelik.jetpackcomposetemplate.data.dao.NearMissDao
import com.ferhatozcelik.jetpackcomposetemplate.data.entity.NearMissEntity
import com.ferhatozcelik.jetpackcomposetemplate.data.remote.api.PplApiService
import com.ferhatozcelik.jetpackcomposetemplate.data.remote.model.NearMissDto
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class NearMissViewModel @Inject constructor(
    private val nearMissDao: NearMissDao,
    private val apiService: PplApiService
) : ViewModel() {

    private val _allNearMisses = MutableStateFlow<List<NearMissEntity>>(emptyList())
    val allNearMisses: StateFlow<List<NearMissEntity>> = _allNearMisses.asStateFlow()

    init {
        fetchRemoteNearMisses()
    }

    fun fetchRemoteNearMisses() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiService.getNearMisses()
                android.util.Log.d("NearMissViewModel", "Fetch response code: ${response.code()}")
                if (response.isSuccessful) {
                    response.body()?.let { dtos ->
                        android.util.Log.d("NearMissViewModel", "Fetched ${dtos.size} near misses")
                        val list = dtos.map { dto ->
                            NearMissEntity(
                                id = dto.id,
                                title = dto.title ?: "Unknown",
                                description = dto.description ?: "No description provided",
                                plantArea = dto.plantArea ?: "Unknown",
                                type = dto.type ?: "Unknown",
                                criticality = dto.criticality?.toIntOrNull() ?: 1,
                                probability = dto.probability?.toIntOrNull() ?: 1,
                                riskScore = dto.riskScore,
                                timestamp = dto.timestamp?.toLongOrNull() ?: System.currentTimeMillis(),
                                photoUrl = dto.photoUrl,
                                resolved = dto.resolved
                            )
                        }
                        _allNearMisses.value = list
                    } ?: android.util.Log.d("NearMissViewModel", "Response body is null!")
                } else {
                    android.util.Log.e("NearMissViewModel", "Fetch error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                android.util.Log.e("NearMissViewModel", "Fetch exception", e)
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
        
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val map = mapOf(
                    "title" to title,
                    "description" to description,
                    "plantArea" to plantArea,
                    "type" to type,
                    "criticality" to criticality.toString(),
                    "probability" to probability.toString(),
                    "riskScore" to riskScore,
                    "timestamp" to System.currentTimeMillis().toString()
                )

                val json = Gson().toJson(map)
                val body = json.toRequestBody("application/json".toMediaTypeOrNull())
                var photoPart: okhttp3.MultipartBody.Part? = null
                photoBitmap?.let { bitmap ->
                    val stream = java.io.ByteArrayOutputStream()
                    bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 90, stream)
                    val byteArray = stream.toByteArray()
                    val requestBody = byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull(), 0, byteArray.size)
                    photoPart = okhttp3.MultipartBody.Part.createFormData("photo", "photo.jpg", requestBody)
                }

                val response = if (photoPart != null) {
                    apiService.submitNearMissWithPhoto(body, photoPart!!)
                } else {
                    apiService.submitNearMissWithoutPhoto(body)
                }
                
                android.util.Log.d("NearMissViewModel", "Submit response code: ${response.code()}")
                if (!response.isSuccessful) {
                    android.util.Log.e("NearMissViewModel", "Submit error: ${response.errorBody()?.string()}")
                }

                // Fetch the updated list from server so the UI gets the correct photoUrl
                fetchRemoteNearMisses()
            } catch (e: Exception) {
                android.util.Log.e("NearMissViewModel", "Submit exception", e)
                e.printStackTrace()
            }
        }
    }

    fun resolveNearMiss(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Call backend
                apiService.resolveNearMiss(id)
                // Fetch the updated list
                fetchRemoteNearMisses()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
