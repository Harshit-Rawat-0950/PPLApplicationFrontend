package com.ferhatozcelik.jetpackcomposetemplate.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ferhatozcelik.jetpackcomposetemplate.data.dao.ShiftLogbookDao
import com.ferhatozcelik.jetpackcomposetemplate.data.entity.AssetData
import com.ferhatozcelik.jetpackcomposetemplate.data.entity.ShiftLogbookEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.ferhatozcelik.jetpackcomposetemplate.data.remote.api.PplApiService
import com.ferhatozcelik.jetpackcomposetemplate.data.remote.model.ShiftLogbookDto
import com.ferhatozcelik.jetpackcomposetemplate.data.remote.model.AssetDataDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltViewModel
class ShiftLogbookViewModel @Inject constructor(
    private val shiftLogbookDao: ShiftLogbookDao,
    private val apiService: PplApiService
) : ViewModel() {

    init {
        fetchRemoteLogbooks()
    }

    val allLogbooks: Flow<List<ShiftLogbookEntity>> = shiftLogbookDao.getAllLogbooks()

    fun fetchRemoteLogbooks() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiService.getLogbooks()
                if (response.isSuccessful) {
                    response.body()?.forEach { dto ->
                        val entity = ShiftLogbookEntity(
                            date = dto.date,
                            shift = dto.shift,
                            area = dto.area,
                            submitterId = dto.submitterId,
                            assets = dto.assets.map {
                                AssetData(
                                    assetTag = it.assetTag,
                                    standingAlarms = it.standingAlarms,
                                    maintenanceStatus = it.maintenanceStatus,
                                    maintenanceDone = it.maintenanceDone
                                )
                            }
                        )
                        shiftLogbookDao.insertLogbook(entity)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun insertLogbook(
        date: String,
        shift: String,
        area: String,
        submitterId: String,
        assets: List<AssetData>
    ) {
        val entity = ShiftLogbookEntity(
            date = date,
            shift = shift,
            area = area,
            submitterId = submitterId,
            assets = assets
        )
        viewModelScope.launch(Dispatchers.IO) {
            // 1. Save locally
            shiftLogbookDao.insertLogbook(entity)
            
            // 2. Push to backend
            try {
                val dto = ShiftLogbookDto(
                    date = date,
                    shift = shift,
                    area = area,
                    submitterId = submitterId,
                    assets = assets.map {
                        AssetDataDto(
                            assetTag = it.assetTag,
                            standingAlarms = it.standingAlarms,
                            maintenanceStatus = it.maintenanceStatus,
                            maintenanceDone = it.maintenanceDone
                        )
                    }
                )
                apiService.submitLogbook(dto)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
