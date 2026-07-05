package com.ferhatozcelik.jetpackcomposetemplate.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shift_logbook_table")
data class ShiftLogbookEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,
    val shift: String,
    val area: String,
    val submitterId: String,
    val assets: List<AssetData>,
    val timestamp: Long = System.currentTimeMillis()
)
