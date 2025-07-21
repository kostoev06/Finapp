package com.finapp.core.work.transaction

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.finapp.core.data.api.repository.TransactionRepository
import com.finapp.core.work.transaction.di.WorkerAssistedFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.finapp.core.common.outcome.handleOutcome
import com.finapp.core.data.api.model.asTransactionBrief
import com.finapp.core.data.api.model.asTransactionInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant

class SyncTransactionWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val transactionRepository: TransactionRepository,
    private val dataStore: DataStore<Preferences>
) : CoroutineWorker(appContext, params) {

    private val lastSync = longPreferencesKey("last_sync")

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val unsyncedOld = transactionRepository.getUnsyncedOldLocalTransactions()
            val unsyncedNew = transactionRepository.getUnsyncedNewLocalTransactions()

            if (unsyncedOld.isNotEmpty()) {
                unsyncedOld.forEach { transaction ->
                    transactionRepository.updateTransaction(transaction.asTransactionBrief()).handleOutcome {
                        onSuccess {
                            transactionRepository.markLocalTransactionSynced(transaction.id!!, data.id!!)
                        }
                        onFailure {
                            return@withContext Result.retry()
                        }
                    }
                }
            }

            if (unsyncedNew.isNotEmpty()) {
                unsyncedNew.forEach { transaction ->
                    transactionRepository.createTransaction(transaction.asTransactionBrief()).handleOutcome {
                        onSuccess {
                            transactionRepository.markLocalTransactionSynced(transaction.id!!, data.id!!)
                        }
                        onFailure {
                            return@withContext Result.retry()
                        }
                    }
                }
            }

            transactionRepository.fetchTransactionsByPeriod(
                transactionRepository.getOldestLocalTransactionDate(),
                transactionRepository.getNewestLocalTransactionDate()
            ).handleOutcome {
                onSuccess {
                    transactionRepository.clearAllLocalTransactions()
                    data.forEach { transaction ->
                        transactionRepository.insertSyncedLocalTransaction(transaction.asTransactionInfo())
                    }
                }
            }

            dataStore.edit { it[lastSync] = Instant.now().epochSecond }

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }


    @AssistedFactory
    interface Factory : WorkerAssistedFactory<SyncTransactionWorker>
}