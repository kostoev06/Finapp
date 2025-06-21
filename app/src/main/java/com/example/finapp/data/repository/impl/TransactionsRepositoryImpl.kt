package com.example.finapp.data.repository.impl

import com.example.finapp.data.common.Outcome
import com.example.finapp.data.common.transform
import com.example.finapp.data.remote.RemoteDataSource
import com.example.finapp.data.remote.dto.toDomain
import com.example.finapp.data.repository.TransactionsRepository
import com.example.finapp.domain.Transaction


class TransactionsRepositoryImpl(
    private val remoteDataSource: RemoteDataSource = RemoteDataSource()
) : TransactionsRepository {

    override suspend fun getTransactions(
        accountId: Long,
        startDate: String?,
        endDate:   String?
    ): Outcome<List<Transaction>> {
        return remoteDataSource
            .fetchTransactions(accountId, startDate, endDate)
            .transform { dtoList ->
                dtoList.map { dto -> dto.toDomain() }
            }
    }
}