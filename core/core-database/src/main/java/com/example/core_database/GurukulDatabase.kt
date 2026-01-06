package com.example.core_database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.core_database.converter.Converters
import com.example.core_database.dao.ClassDao
import com.example.core_database.entity.ClassEntity

@Database(
    entities = [ClassEntity::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class GurukulDatabase : RoomDatabase() {
    abstract fun classDao(): ClassDao
}