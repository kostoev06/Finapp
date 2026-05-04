package com.finapp.core.domain.usecase

import com.finapp.core.common.outcome.Outcome
import com.finapp.core.data.api.model.Transaction
import com.finapp.core.data.api.model.asTransactionInfo
import com.finapp.core.data.api.repository.TransactionRepository
import com.finapp.core.domain.result.LoadResult
import javax.inject.Inject

/**
 * Транзакция по id; на ошибке — из локального кеша. Успех зеркалит в кеш.
 *
 * `data == null` означает «нет ни на сервере, ни в кеше» — обычно это ошибочный id или
 * запись была удалена с сервера, а локально у нас её ещё не было.
 */
class GetTransactionByIdUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(id: Long): LoadResult<Transaction?> {
        return when (val outcome = transactionRepository.fetchTransactionById(id)) {
            is Outcome.Success -> {
                transactionRepository.insertSyncedLocalTransaction(outcome.data.asTransactionInfo())
                LoadResult(data = outcome.data)
            }
            is Outcome.FailureOutcome -> LoadResult(
                data = transactionRepository.getLocalTransactionById(id),
                error = outcome
            )
        }
    }
}
