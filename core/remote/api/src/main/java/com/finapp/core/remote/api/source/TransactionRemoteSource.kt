package com.finapp.core.remote.api.source

import com.finapp.core.common.outcome.Outcome
import com.finapp.core.remote.api.model.TransactionDto
import com.finapp.core.remote.api.model.TransactionRequest
import com.finapp.core.remote.api.model.TransactionResponse


interface TransactionRemoteSource {
    suspend fun fetchTransactionsByPeriod(
        accountId: Long,
        startDate: String?,
        endDate: String?
    ): Outcome<List<TransactionResponse>>

    suspend fun createTransaction(
        request: TransactionRequest
    ): Outcome<TransactionDto>

    suspend fun updateTransaction(
        id: Long,
        request: TransactionRequest
    ): Outcome<TransactionResponse>

    suspend fun fetchTransactionById(
        id: Long
    ): Outcome<TransactionResponse>
}