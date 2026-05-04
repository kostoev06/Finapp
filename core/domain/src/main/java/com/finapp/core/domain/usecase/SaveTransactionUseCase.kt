package com.finapp.core.domain.usecase

import com.finapp.core.common.outcome.Outcome
import com.finapp.core.data.api.model.TransactionBrief
import com.finapp.core.data.api.model.TransactionInfo
import com.finapp.core.data.api.model.asTransactionInfo
import com.finapp.core.data.api.repository.AccountIdProvider
import com.finapp.core.data.api.repository.TransactionRepository
import javax.inject.Inject

/**
 * Сохраняет транзакцию (создаёт, если `brief.id == null`, иначе обновляет).
 *
 * Контракт: даже если бэкенд недоступен, локальный кеш всегда обновляется — пользователь
 * увидит свою запись сразу, [SyncTransactionWorker][com.finapp.core.work.transaction.SyncTransactionWorker]
 * протолкнёт её на сервер позже.
 *
 * Поведение:
 * - **Создание + успех** → `insertSyncedLocalTransaction(info_from_server)` (id известный, isSynced=true).
 * - **Создание + ошибка** → `insertUnsyncedLocalTransaction(local_info)` (isSynced=false, isNew=true) → воркер сделает POST.
 * - **Обновление + успех** → `updateSyncedLocalTransaction(info_from_server)` (запишется как «синхронизировано»).
 * - **Обновление + ошибка**: если ряд в кеше уже был synced — `updateSyncedLocalTransaction(local_info)` (помечается isSynced=false, isNew=false → воркер сделает PUT); если был unsynced new — `updateUnsyncedLocalTransaction` (остаётся isNew=true → воркер сделает POST).
 */
class SaveTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountIdProvider: AccountIdProvider
) {
    suspend operator fun invoke(brief: TransactionBrief): Result =
        if (brief.id == null) create(brief) else update(brief, brief.id!!)

    private suspend fun create(brief: TransactionBrief): Result {
        return when (val outcome = transactionRepository.createTransaction(brief)) {
            is Outcome.Success -> {
                transactionRepository.insertSyncedLocalTransaction(outcome.data)
                Result.Synced
            }
            is Outcome.FailureOutcome -> {
                transactionRepository.insertUnsyncedLocalTransaction(
                    brief.toLocalInfo(accountId = accountIdProvider.get())
                )
                Result.Offline(outcome)
            }
        }
    }

    private suspend fun update(brief: TransactionBrief, id: Long): Result {
        return when (val outcome = transactionRepository.updateTransaction(brief)) {
            is Outcome.Success -> {
                transactionRepository.updateSyncedLocalTransaction(outcome.data.asTransactionInfo())
                Result.Synced
            }
            is Outcome.FailureOutcome -> {
                val localInfo = brief.toLocalInfo(accountId = accountIdProvider.get(), idOverride = id)
                if (transactionRepository.getSyncedLocalTransactionById(id) != null) {
                    transactionRepository.updateSyncedLocalTransaction(localInfo)
                } else {
                    transactionRepository.updateUnsyncedLocalTransaction(localInfo)
                }
                Result.Offline(outcome)
            }
        }
    }

    sealed interface Result {
        /** Сохранено и на сервере, и в локальном кеше. */
        data object Synced : Result

        /** Сохранено только локально; воркер протолкнёт позже. UI обычно показывает [error] снэкбаром. */
        data class Offline(val error: Outcome.FailureOutcome) : Result
    }
}

/**
 * `createdAt` / `updatedAt` ставим равными `transactionDate` — повторяет поведение исходного
 * VM-кода: сервер сам проставит реальные времена при следующей синхронизации.
 */
private fun TransactionBrief.toLocalInfo(accountId: Long, idOverride: Long? = null): TransactionInfo =
    TransactionInfo(
        id = idOverride ?: id,
        accountId = accountId,
        categoryId = categoryId,
        amount = amount,
        transactionDate = transactionDate,
        comment = comment,
        createdAt = transactionDate,
        updatedAt = transactionDate
    )
