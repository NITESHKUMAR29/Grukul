package com.example.core_database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.core_database.dao.ClassDao
import com.example.core_database.entity.ClassEntity

@Database(
    entities = [ClassEntity::class],
    version = 1,
    exportSchema = false
)
abstract class GurukulDatabase : RoomDatabase() {
    abstract fun classDao(): ClassDao
}