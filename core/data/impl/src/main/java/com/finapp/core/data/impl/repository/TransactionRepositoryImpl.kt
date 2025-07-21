package com.finapp.core.data.impl.repository

import com.finapp.core.common.outcome.Outcome
import com.finapp.core.common.outcome.transform
import com.finapp.core.data.api.model.Transaction
import com.finapp.core.data.api.model.TransactionBrief
import com.finapp.core.data.api.model.TransactionInfo
import com.finapp.core.data.api.repository.TransactionRepository
import com.finapp.core.data.impl.BuildConfig
import com.finapp.core.data.impl.model.asTransaction
import com.finapp.core.data.impl.model.asTransactionEntity
import com.finapp.core.data.impl.model.asTransactionInfo
import com.finapp.core.data.impl.model.asTransactionRequest
import com.finapp.core.database.api.source.TransactionLocalSource
import com.finapp.core.remote.api.source.TransactionRemoteSource
import jakarta.inject.Inject
import javax.inject.Singleton

/**
 * Репозиторий транзакций.
 */
@Singleton
class TransactionRepositoryImpl @Inject constructor(
    private val transactionRemoteSource: TransactionRemoteSource,
    private val transactionLocalSource: TransactionLocalSource
) : TransactionRepository {

    override suspend fun fetchTransactionsByPeriod(
        startDate: String?,
        endDate: String?
    ): Outcome<List<Transaction>> {
        return transactionRemoteSource
            .fetchTransactionsByPeriod(
                BuildConfig.ACCOUNT_ID,
                startDate,
                endDate
            )
            .transform { dtoList ->
                dtoList.map { dto -> dto.asTransaction() }
            }
    }

    override suspend fun createTransaction(
        transactionBrief: TransactionBrief
    ): Outcome<TransactionInfo> {
        return transactionRemoteSource.createTransaction(
            transactionBrief.asTransactionRequest(BuildConfig.ACCOUNT_ID)
        ).transform { it.asTransactionInfo() }
    }

    override suspend fun updateTransaction(
        transactionBrief: TransactionBrief
    ): Outcome<Transaction> {
        return transactionRemoteSource.updateTransaction(
            transactionBrief.id!!, transactionBrief.asTransactionRequest(BuildConfig.ACCOUNT_ID)
        ).transform { it.asTransaction() }
    }

    override suspend fun fetchTransactionById(id: Long): Outcome<Transaction> {
        return transactionRemoteSource.fetchTransactionById(id).transform {
            it.asTransaction()
        }
    }

    override suspend fun getLocalTransactionsByPeriod(
        startIso: String,
        endIso: String
    ): List<Transaction> =
        transactionLocalSource.getByPeriod(startIso, endIso)
            .map { it.asTransaction() }

    override suspend fun getLocalTransactionById(id: Long): Transaction? =
        transactionLocalSource.getById(id)?.asTransaction()

    override suspend fun getSyncedLocalTransactionById(id: Long): Transaction? =
        transactionLocalSource.getSyncedById(id)?.asTransaction()

    override suspend fun getUnsyncedOldLocalTransactions(): List<Transaction> =
        transactionLocalSource.getUnsyncedOld()
            .map { it.asTransaction() }

    override suspend fun getUnsyncedNewLocalTransactions(): List<Transaction> =
        transactionLocalSource.getUnsyncedNew()
            .map { it.asTransaction() }

    override suspend fun getOldestLocalTransactionDate(): String? =
        transactionLocalSource.getOldestDate()

    override suspend fun getNewestLocalTransactionDate(): String? =
        transactionLocalSource.getNewestDate()

    override suspend fun insertSyncedLocalTransaction(transaction: TransactionInfo): Long =
        transactionLocalSource.insert(transaction.asTransactionEntity(isSynced = true, isNew = false))

    override suspend fun insertUnsyncedLocalTransaction(transaction: TransactionInfo): Long =
        transactionLocalSource.insert(transaction.asTransactionEntity(isSynced = false, isNew = true))

    override suspend fun updateSyncedLocalTransaction(transaction: TransactionInfo) =
        transactionLocalSource.update(transaction.asTransactionEntity(isSynced = false, isNew = false))

    override suspend fun updateUnsyncedLocalTransaction(transaction: TransactionInfo) =
        transactionLocalSource.update(transaction.asTransactionEntity(isSynced = false, isNew = true))

    override suspend fun markLocalTransactionSynced(
        tableId: Long,
        backendId: Long
    ) {
        transactionLocalSource.markSynced(tableId, backendId)
    }

    override suspend fun deleteLocalTransaction(tableId: Long) {
        transactionLocalSource.delete(tableId)
    }

    override suspend fun clearAllLocalTransactions() {
        transactionLocalSource.deleteAll()
    }
}