package com.finapp.core.work.transaction

import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncTransactionScheduler @Inject constructor(
    private val workManager: WorkManager
) {
    fun scheduleOneShot() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = OneTimeWorkRequestBuilder<SyncTransactionWorker>()
            .setConstraints(constraints)
            .setExpedited(
                OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST
            )
            .addTag("sync_transactions")
            .build()

        workManager.enqueueUniqueWork(
            "sync_transactions",
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    fun schedulePeriodic() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodic = PeriodicWorkRequestBuilder<SyncTransactionWorker>(
            2, TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                30, TimeUnit.SECONDS
            )
            .addTag("sync_transactions_periodic")
            .build()

        workManager.enqueueUniquePeriodicWork(
            "sync_transactions_periodic",
            ExistingPeriodicWorkPolicy.KEEP,
            periodic
        )
    }
}