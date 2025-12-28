package com.example.gurukul.di

import android.content.Context
import com.example.core_common.workScheduler.SyncScheduler
import com.example.gurukul.AppSyncScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SyncModule {

    @Provides
    @Singleton
    fun provideSyncScheduler(
        @ApplicationContext context: Context
    ): SyncScheduler = AppSyncScheduler(context)
}