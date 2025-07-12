package com.finapp.core.data.api.repository

import com.finapp.core.common.outcome.Outcome
import com.finapp.core.data.api.model.Transaction
import com.finapp.core.data.api.model.TransactionBrief

interface TransactionRepository {
    suspend fun getTransactionsByPeriod(
        startDate: String?,
        endDate: String?
    ): Outcome<List<Transaction>>

    suspend fun createTransaction(
        categoryId: Long,
        amount: String,
        transactionDateIso: String,
        comment: String?
    ): Outcome<TransactionBrief>

    suspend fun updateTransaction(
        id: Long,
        categoryId: Long,
        amount: String,
        transactionDateIso: String,
        comment: String?
    ): Outcome<Transaction>

    suspend fun getTransactionById(
        id: Long
    ): Outcome<Transaction>
}
