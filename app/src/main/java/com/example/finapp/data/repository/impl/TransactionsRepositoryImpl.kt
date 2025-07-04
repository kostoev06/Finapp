package com.example.finapp.data.repository.impl

import com.example.finapp.BuildConfig
import com.example.finapp.data.common.Outcome
import com.example.finapp.data.common.transform
import com.example.finapp.data.remote.TransactionsRemoteDataSource
import com.example.finapp.data.remote.dto.toDomain
import com.example.finapp.data.remote.retrofit.RetrofitTransactionsRemoteDataSource
import com.example.finapp.data.repository.TransactionsRepository
import com.example.finapp.domain.Transaction


/**
 * Репозиторий транзакций.
 */
class TransactionsRepositoryImpl(
    private val transactionsRemoteDataSource: TransactionsRemoteDataSource = RetrofitTransactionsRemoteDataSource()
) : TransactionsRepository {

    override suspend fun getTransactions(
        startDate: String?,
        endDate:   String?
    ): Outcome<List<Transaction>> {
        return transactionsRemoteDataSource
            .fetchTransactionsByPeriod(
                BuildConfig.ACCOUNT_ID,
                startDate,
                endDate)
            .transform { dtoList ->
                dtoList.map { dto -> dto.toDomain() }
            }
    }
}