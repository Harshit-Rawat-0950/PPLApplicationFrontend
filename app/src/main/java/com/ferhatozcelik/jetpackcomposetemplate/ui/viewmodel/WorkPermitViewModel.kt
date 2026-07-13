package com.ferhatozcelik.jetpackcomposetemplate.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ferhatozcelik.jetpackcomposetemplate.data.remote.api.PplApiService
import com.ferhatozcelik.jetpackcomposetemplate.data.remote.model.UserDto
import com.ferhatozcelik.jetpackcomposetemplate.data.remote.model.WorkPermitApplyDto
import com.ferhatozcelik.jetpackcomposetemplate.data.remote.model.WorkPermitApproveDto
import com.ferhatozcelik.jetpackcomposetemplate.data.remote.model.WorkPermitDto
import com.ferhatozcelik.jetpackcomposetemplate.util.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkPermitViewModel @Inject constructor(
    private val apiService: PplApiService,
    private val sessionManager: SessionManager
) : ViewModel() {

    val userRole = sessionManager.getUserRole() ?: "WORKER"
    val userId = sessionManager.getUserId() ?: ""

    private val _maintenanceHeads = MutableStateFlow<List<UserDto>>(emptyList())
    val maintenanceHeads: StateFlow<List<UserDto>> = _maintenanceHeads

    private val _operators = MutableStateFlow<List<UserDto>>(emptyList())
    val operators: StateFlow<List<UserDto>> = _operators

    private val _workPermits = MutableStateFlow<List<WorkPermitDto>>(emptyList())
    val workPermits: StateFlow<List<WorkPermitDto>> = _workPermits

    init {
        fetchWorkPermits()
        if (userRole == "WORKER") {
            fetchMaintenanceHeads()
        } else if (userRole == "MAINTENANCE_HEAD") {
            fetchOperators()
        }
    }

    fun fetchMaintenanceHeads() {
        viewModelScope.launch {
            try {
                val response = apiService.getUsersByRole(null)
                if (response.isSuccessful) {
                    _maintenanceHeads.value = response.body()?.filter { it.role.endsWith("HEAD") } ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchOperators() {
        viewModelScope.launch {
            try {
                val response = apiService.getUsersByRole("OPERATOR")
                if (response.isSuccessful) {
                    _operators.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchWorkPermits() {
        viewModelScope.launch {
            try {
                val response = apiService.getWorkPermits()
                if (response.isSuccessful) {
                    _workPermits.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun applyForPermit(assetName: String, taskDescription: String, date: String, shift: String, site: String, approverId: String) {
        viewModelScope.launch {
            try {
                val dto = WorkPermitApplyDto(assetName, taskDescription, date, shift, site, userId, approverId)
                apiService.applyForWorkPermit(dto)
                fetchWorkPermits()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun approvePermit(permitId: Long, assignedOperatorId: String, instructions: String) {
        viewModelScope.launch {
            try {
                val dto = WorkPermitApproveDto(assignedOperatorId, instructions)
                apiService.approveWorkPermit(permitId, dto)
                fetchWorkPermits()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun rejectPermit(permitId: Long) {
        viewModelScope.launch {
            try {
                apiService.rejectPermit(permitId)
                fetchWorkPermits()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun verifyLoto(
        permitId: Long,
        lotoVerified: Boolean,
        o2Reading: Double?,
        lelReading: Double?,
        coReading: Double?,
        h2sNh3Reading: Double?,
        operatorPin: String
    ) {
        viewModelScope.launch {
            try {
                val dto = com.ferhatozcelik.jetpackcomposetemplate.data.remote.model.WorkPermitVerifyLotoDto(
                    lotoVerified, o2Reading, lelReading, coReading, h2sNh3Reading, operatorPin
                )
                apiService.verifyLoto(permitId, dto)
                fetchWorkPermits()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun closePermit(permitId: Long, requestingAuthorityPin: String) {
        viewModelScope.launch {
            try {
                val dto = com.ferhatozcelik.jetpackcomposetemplate.data.remote.model.WorkPermitCloseDto(requestingAuthorityPin)
                apiService.closePermit(permitId, dto)
                fetchWorkPermits()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
