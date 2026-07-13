package com.ferhatozcelik.jetpackcomposetemplate.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ferhatozcelik.jetpackcomposetemplate.data.dao.ShiftLogbookDao
import com.ferhatozcelik.jetpackcomposetemplate.data.entity.AssetData
import com.ferhatozcelik.jetpackcomposetemplate.data.entity.ShiftLogbookEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _allLogbooks = MutableStateFlow<List<ShiftLogbookEntity>>(emptyList())
    val allLogbooks: StateFlow<List<ShiftLogbookEntity>> = _allLogbooks.asStateFlow()

    init {
        fetchRemoteLogbooks()
    }

    fun fetchRemoteLogbooks() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiService.getLogbooks()
                if (response.isSuccessful) {
                    response.body()?.let { dtos ->
                        val list = dtos.map { dto ->
                            ShiftLogbookEntity(
                                id = dto.id,
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
                        }
                        _allLogbooks.value = list
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
        viewModelScope.launch(Dispatchers.IO) {
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
                val response = apiService.submitLogbook(dto)
                if (response.isSuccessful) {
                    fetchRemoteLogbooks()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
