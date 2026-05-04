package com.finapp.core.domain.usecase

import com.finapp.core.common.outcome.Outcome
import com.finapp.core.data.api.model.Account
import com.finapp.core.data.api.repository.AccountRepository
import com.finapp.core.domain.result.LoadResult
import javax.inject.Inject

/**
 * Берёт аккаунт с сервера; на ошибке отдаёт последний локально закешированный.
 * Успешный ответ зеркалит в локальный источник.
 *
 * `data == null` означает «нет ни в сети, ни в кеше» — например, первый запуск без сети.
 * VM в этом случае обычно показывает ошибку из [LoadResult.error].
 */
class GetAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(): LoadResult<Account?> {
        return when (val outcome = accountRepository.fetchAccount()) {
            is Outcome.Success -> {
                accountRepository.insertLocalAccount(outcome.data)
                LoadResult(data = outcome.data)
            }
            is Outcome.FailureOutcome -> LoadResult(
                data = accountRepository.getLocalAccount(),
                error = outcome
            )
        }
    }
}
