package com.example.feature_class.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.feature_auth.domain.repositories.UserLocalRepository
import com.example.feature_class.domain.repository.ClassRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class ClassSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: ClassRepository,
    private val userLocalRepository: UserLocalRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            Log.d("ClassSyncWorker", "Syncing classes...")
            val createdBy = userLocalRepository.observeUserId().first()
                ?: return Result.success()
            repository.syncClasses(createdBy)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}