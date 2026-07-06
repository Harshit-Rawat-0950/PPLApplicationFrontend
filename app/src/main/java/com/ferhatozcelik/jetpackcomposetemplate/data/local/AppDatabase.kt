package com.ferhatozcelik.jetpackcomposetemplate.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ferhatozcelik.jetpackcomposetemplate.data.dao.ExampleDao
import com.ferhatozcelik.jetpackcomposetemplate.data.dao.NearMissDao
import com.ferhatozcelik.jetpackcomposetemplate.data.entity.ExampleEntity
import com.ferhatozcelik.jetpackcomposetemplate.data.entity.NearMissEntity
import com.ferhatozcelik.jetpackcomposetemplate.data.entity.ShiftLogbookEntity
import com.ferhatozcelik.jetpackcomposetemplate.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [ExampleEntity::class, NearMissEntity::class, ShiftLogbookEntity::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getExampleDao(): ExampleDao
    abstract fun getNearMissDao(): NearMissDao
    abstract fun getShiftLogbookDao(): com.ferhatozcelik.jetpackcomposetemplate.data.dao.ShiftLogbookDao

    class Callback @Inject constructor(
        private val database: Provider<AppDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback()
}