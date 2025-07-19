package com.finapp.core.data.impl.repository

import com.finapp.core.common.outcome.Outcome
import com.finapp.core.common.outcome.transform
import com.finapp.core.data.api.model.Transaction
import com.finapp.core.data.api.model.TransactionBrief
import com.finapp.core.data.api.repository.TransactionRepository
import com.finapp.core.data.impl.BuildConfig
import com.finapp.core.data.impl.model.asTransaction
import com.finapp.core.data.impl.model.asTransactionBrief
import com.finapp.core.remote.api.model.TransactionRequest
import com.finapp.core.remote.api.source.TransactionRemoteSource
import jakarta.inject.Inject
import javax.inject.Singleton

/**
 * Репозиторий транзакций.
 */
@Singleton
class TransactionRepositoryImpl @Inject constructor(
    private val transactionRemoteSource: TransactionRemoteSource
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
        categoryId: Long,
        amount: String,
        transactionDateIso: String,
        comment: String?
    ): Outcome<TransactionBrief> {
        return transactionRemoteSource.createTransaction(
            TransactionRequest(BuildConfig.ACCOUNT_ID, categoryId, amount, transactionDateIso, comment)
        ).transform { it.asTransactionBrief() }
    }

    override suspend fun updateTransaction(
        id: Long,
        categoryId: Long,
        amount: String,
        transactionDateIso: String,
        comment: String?
    ): Outcome<Transaction> {
        return transactionRemoteSource.updateTransaction(
            id, TransactionRequest(BuildConfig.ACCOUNT_ID, categoryId, amount, transactionDateIso, comment)
        ).transform { it.asTransaction() }
    }

    override suspend fun fetchTransactionById(id: Long): Outcome<Transaction> {
        return transactionRemoteSource.fetchTransactionById(id).transform {
            it.asTransaction()
        }
    }
}