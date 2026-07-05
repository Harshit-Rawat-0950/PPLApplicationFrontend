package com.ferhatozcelik.jetpackcomposetemplate.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ferhatozcelik.jetpackcomposetemplate.data.entity.NearMissEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NearMissDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNearMiss(nearMiss: NearMissEntity)

    @Query("SELECT * FROM near_miss_table ORDER BY timestamp DESC")
    fun getAllNearMisses(): Flow<List<NearMissEntity>>
}
