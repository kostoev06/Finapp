package com.finapp.core.domain.usecase

import com.finapp.core.common.outcome.Outcome
import com.finapp.core.data.api.model.Account
import com.finapp.core.data.api.repository.AccountRepository
import com.finapp.core.data.api.repository.CurrencyRepository
import javax.inject.Inject

/**
 * Изменить аккаунт на сервере и распространить новую валюту в [CurrencyRepository], чтобы
 * экраны Expenses/Income/Account сразу перерисовали символ. Также пишет фрешевый объект в
 * локальный кеш — иначе после смены валюты Room остаётся со старой версией до следующего
 * SyncWorker-тика.
 */
class UpdateAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val currencyRepository: CurrencyRepository
) {
    suspend operator fun invoke(account: Account): Outcome<Account> {
        return when (val outcome = accountRepository.updateAccount(account)) {
            is Outcome.Success -> {
                accountRepository.insertLocalAccount(outcome.data)
                currencyRepository.setCurrency(outcome.data.currency.code)
                outcome
            }
            is Outcome.FailureOutcome -> outcome
        }
    }
}
