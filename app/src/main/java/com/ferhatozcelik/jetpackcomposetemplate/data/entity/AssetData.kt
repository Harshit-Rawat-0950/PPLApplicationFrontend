package com.ferhatozcelik.jetpackcomposetemplate.data.entity

data class AssetData(
    val assetTag: String,
    val standingAlarms: String,
    val maintenanceStatus: Int, // 1 to 5
    val maintenanceDone: String
)
