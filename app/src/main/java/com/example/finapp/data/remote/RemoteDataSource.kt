package com.example.finapp.data.remote

import com.example.finapp.data.common.Outcome
import com.example.finapp.data.remote.dto.CategoryDto
import com.example.finapp.data.remote.dto.TransactionResponse
import com.example.finapp.data.remote.outcome.asRemoteResult

class RemoteDataSource(
    private val api: ApiService = NetworkModule.apiService
) {
    suspend fun fetchTransactions(
        accountId: Long,
        startDate: String?,
        endDate: String?
    ): Outcome<List<TransactionResponse>> {
        return try {
            api.getTransactionsByPeriod(accountId, startDate, endDate)
                .asRemoteResult()
        } catch (t: Throwable) {
            Outcome.Exception(t)
        }
    }

    suspend fun fetchAllCategories(): Outcome<List<CategoryDto>> {
        return try {
            api.getAllCategories()
                .asRemoteResult()
        } catch (t: Throwable) {
            Outcome.Exception(t)
        }
    }
}