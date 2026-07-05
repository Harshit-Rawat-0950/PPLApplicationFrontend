package com.ferhatozcelik.jetpackcomposetemplate.data.local

import androidx.room.TypeConverter
import java.util.Date

class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date {
        return value?.let { Date(it) } ?: Date(System.currentTimeMillis())
    }

    @TypeConverter
    fun toTimestamp(value: Date?): Long {
        return value?.let { value.time } ?: System.currentTimeMillis()
    }

    @TypeConverter
    fun fromAssetDataList(value: List<com.ferhatozcelik.jetpackcomposetemplate.data.entity.AssetData>?): String {
        return com.google.gson.Gson().toJson(value)
    }

    @TypeConverter
    fun toAssetDataList(value: String): List<com.ferhatozcelik.jetpackcomposetemplate.data.entity.AssetData> {
        val listType = object : com.google.gson.reflect.TypeToken<List<com.ferhatozcelik.jetpackcomposetemplate.data.entity.AssetData>>() {}.type
        return com.google.gson.Gson().fromJson(value, listType) ?: emptyList()
    }
}