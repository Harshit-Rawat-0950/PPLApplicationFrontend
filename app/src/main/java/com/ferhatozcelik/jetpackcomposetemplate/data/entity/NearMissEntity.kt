package com.ferhatozcelik.jetpackcomposetemplate.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "near_miss_table")
data class NearMissEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val plantArea: String,
    val type: String,
    val criticality: Int,
    val probability: Int,
    val riskScore: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val photoUrl: String? = null,
    val resolved: Boolean = false
)
