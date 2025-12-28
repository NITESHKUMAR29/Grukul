package com.example.core_database.di

import android.content.Context
import androidx.room.Room
import com.example.core_database.GurukulDatabase
import com.example.core_database.dao.ClassDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): GurukulDatabase =
        Room.databaseBuilder(
            context,
            GurukulDatabase::class.java,
            "gurukul_db"
        ).build()

    @Provides
    fun provideClassDao(
        database: GurukulDatabase
    ): ClassDao = database.classDao()
}