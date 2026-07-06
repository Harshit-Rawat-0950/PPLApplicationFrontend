package com.ferhatozcelik.jetpackcomposetemplate.data.remote.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val userId: String,
    val role: String
)

data class NearMissDto(
    val id: Int = 0,
    val title: String,
    val description: String,
    val plantArea: String,
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
