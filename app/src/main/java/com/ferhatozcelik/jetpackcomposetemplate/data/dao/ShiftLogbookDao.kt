package com.ferhatozcelik.jetpackcomposetemplate.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ferhatozcelik.jetpackcomposetemplate.data.entity.ShiftLogbookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShiftLogbookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogbook(logbook: ShiftLogbookEntity)

    @Query("DELETE FROM shift_logbook_table")
    suspend fun deleteAllLogbooks()

    @Query("SELECT * FROM shift_logbook_table ORDER BY timestamp DESC")
    fun getAllLogbooks(): Flow<List<ShiftLogbookEntity>>
    
    @Query("SELECT * FROM shift_logbook_table WHERE id = :logbookId")
    suspend fun getLogbookById(logbookId: Int): ShiftLogbookEntity?
}
