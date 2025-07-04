package com.example.finapp.data.repository

import com.example.finapp.data.common.Outcome
import com.example.finapp.domain.Transaction

interface TransactionsRepository {
    suspend fun getTransactions(
        startDate: String?,
        endDate: String?
    ): Outcome<List<Transaction>>
}
