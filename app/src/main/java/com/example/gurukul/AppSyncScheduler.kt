package com.example.gurukul

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.core_common.workScheduler.SyncScheduler
import com.example.feature_class.data.worker.ClassSyncWorker
import java.util.concurrent.TimeUnit

class AppSyncScheduler(
    private val context: Context
) : SyncScheduler {

    override fun scheduleAll() {
        scheduleClassSync()
        //scheduleStudentSync()
        // future workers go here
    }

    override fun cancelAll() {
        WorkManager.getInstance(context)
            .cancelUniqueWork("class_sync")

        WorkManager.getInstance(context)
            .cancelUniqueWork("student_sync")
    }

    private fun scheduleClassSync() {
        enqueuePeriodic<ClassSyncWorker>("class_sync")
    }

//    private fun scheduleStudentSync() {
//        enqueuePeriodic<StudentSyncWorker>("student_sync")
//    }

    private inline fun <reified T : ListenableWorker> enqueuePeriodic(
        name: String
    ) {
        val request = PeriodicWorkRequestBuilder<T>(
            15, TimeUnit.MINUTES
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                name,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
    }
}
