package com.example.finapp.data.remote

import com.example.finapp.data.common.Outcome
import com.example.finapp.data.remote.dto.TransactionResponse

interface TransactionsRemoteDataSource {
    suspend fun fetchTransactionsByPeriod(
        accountId: Long,
        startDate: String?,
        endDate: String?
    ): Outcome<List<TransactionResponse>>
}
