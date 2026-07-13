package com.ferhatozcelik.jetpackcomposetemplate.data.remote.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val userId: String,
    val name: String,
    val role: String
)

data class NearMissDto(
    val id: Int = 0,
    val title: String,
    val description: String,
    val plantArea: String,
    val type: String,
    val criticality: String,
    val probability: String,
    val riskScore: Int,
    val timestamp: String,
    val photoUrl: String? = null,
    val resolved: Boolean = false
)

data class AssetDataDto(
    val assetTag: String,
    val standingAlarms: String,
    val maintenanceStatus: Int,
    val maintenanceDone: String
)

data class ShiftLogbookDto(
    val date: String,
    val shift: String,
    val area: String,
    val submitterId: String,
    val assets: List<AssetDataDto> = emptyList()
)

data class WorkPermitApplyDto(
    val assetName: String,
    val taskDescription: String,
    val date: String,
    val shift: String,
    val site: String,
    val applicantId: String,
    val approverId: String
)

data class WorkPermitApproveDto(
    val assignedOperatorId: String,
    val instructions: String
)

data class WorkPermitDto(
    val id: Long,
    val assetName: String,
    val taskDescription: String,
    val date: String,
    val shift: String,
    val site: String,
    val applicantId: String,
    val approverId: String,
    val assignedOperatorId: String?,
    val instructions: String?,
    val status: String,
    val lotoVerified: Boolean = false,
    val o2Reading: Double? = null,
    val lelReading: Double? = null,
    val coReading: Double? = null,
    val h2sNh3Reading: Double? = null,
    val operatorSignaturePin: String? = null,
    val requestingAuthoritySignaturePin: String? = null
)

data class WorkPermitVerifyLotoDto(
    val lotoVerified: Boolean,
    val o2Reading: Double?,
    val lelReading: Double?,
    val coReading: Double?,
    val h2sNh3Reading: Double?,
    val operatorPin: String
)

data class WorkPermitCloseDto(
    val requestingAuthorityPin: String
)

data class UserDto(
    val id: Long,
    val name: String,
    val username: String,
    val employeeId: String,
    val role: String
)
