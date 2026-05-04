package com.finapp.core.domain.usecase

import com.finapp.core.common.outcome.Outcome
import com.finapp.core.data.api.model.Transaction
import com.finapp.core.data.api.model.asTransactionInfo
import com.finapp.core.data.api.repository.TransactionRepository
import com.finapp.core.domain.result.LoadResult
import com.finapp.core.domain.time.atEndOfDayIsoUtc
import com.finapp.core.domain.time.atStartOfDayIsoUtc
import java.time.LocalDate
import javax.inject.Inject

/**
 * Транзакции за период от `start` до `end` включительно по обоим концам.
 * На ошибке — из локального кеша; успех зеркалит в кеш.
 *
 * Сетевой API принимает даты `yyyy-MM-dd`; Room хранит ISO-OffsetDateTime в UTC, поэтому
 * для локального источника границы расширяются до начала и конца суток в UTC.
 */
class GetTransactionsByPeriodUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(
        start: LocalDate,
        end: LocalDate
    ): LoadResult<List<Transaction>> {
        return when (
            val outcome = transactionRepository.fetchTransactionsByPeriod(
                startDate = start.toString(),
                endDate = end.toString()
            )
        ) {
            is Outcome.Success -> {
                outcome.data.forEach {
                    transactionRepository.insertSyncedLocalTransaction(it.asTransactionInfo())
                }
                LoadResult(data = outcome.data)
            }
            is Outcome.FailureOutcome -> LoadResult(
                data = transactionRepository.getLocalTransactionsByPeriod(
                    startIso = start.atStartOfDayIsoUtc(),
                    endIso = end.atEndOfDayIsoUtc()
                ),
                error = outcome
            )
        }
    }
}
