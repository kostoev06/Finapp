package com.finapp.core.data.api.repository

import com.finapp.core.common.outcome.Outcome
import com.finapp.core.data.api.model.Transaction
import com.finapp.core.data.api.model.TransactionBrief
import com.finapp.core.data.api.model.TransactionInfo

interface TransactionRepository {
    suspend fun fetchTransactionsByPeriod(
        startDate: String?,
        endDate: String?
    ): Outcome<List<Transaction>>

    suspend fun createTransaction(
        transactionBrief: TransactionBrief
    ): Outcome<TransactionInfo>

    suspend fun updateTransaction(
        transactionBrief: TransactionBrief
    ): Outcome<Transaction>

    suspend fun fetchTransactionById(
        id: Long
    ): Outcome<Transaction>

    suspend fun getLocalTransactionsByPeriod(
        startIso: String,
        endIso: String
    ): List<Transaction>

    suspend fun getLocalTransactionById(id: Long): Transaction?

    suspend fun getSyncedLocalTransactionById(id: Long): Transaction?

    suspend fun getUnsyncedOldLocalTransactions(): List<Transaction>

    suspend fun getUnsyncedNewLocalTransactions(): List<Transaction>

    suspend fun getOldestLocalTransactionDate(): String?

    suspend fun getNewestLocalTransactionDate(): String?

    suspend fun insertSyncedLocalTransaction(transaction: TransactionInfo): Long

    suspend fun insertUnsyncedLocalTransaction(transaction: TransactionInfo): Long

    suspend fun updateSyncedLocalTransaction(transaction: TransactionInfo)

    suspend fun updateUnsyncedLocalTransaction(transaction: TransactionInfo)

    suspend fun markLocalTransactionSynced(
        tableId: Long,
        backendId: Long
    )

    suspend fun deleteLocalTransaction(tableId: Long)

    suspend fun clearAllLocalTransactions()
}
