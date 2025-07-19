package com.finapp.core.data.impl.repository

import com.finapp.core.common.outcome.Outcome
import com.finapp.core.common.outcome.transform
import com.finapp.core.data.api.model.Account
import com.finapp.core.data.api.repository.AccountRepository
import com.finapp.core.data.impl.BuildConfig
import com.finapp.core.data.impl.model.asAccount
import com.finapp.core.remote.api.model.AccountUpdateRequest
import com.finapp.core.remote.api.source.AccountRemoteSource
import jakarta.inject.Inject
import javax.inject.Singleton

/**
 * Репозиторий счета.
 */
@Singleton
class AccountRepositoryImpl @Inject constructor(
    private val accountRemoteSource: AccountRemoteSource
) : AccountRepository {
    override suspend fun fetchAccount(): Outcome<Account> =
        accountRemoteSource.fetchAccountById(BuildConfig.ACCOUNT_ID)
            .transform { dto -> dto.asAccount() }

    override suspend fun updateAccount(
        name: String,
        balance: String,
        currency: String
    ): Outcome<Account> {
        val request = AccountUpdateRequest(
            name = name,
            balance = balance,
            currency = currency
        )
        return accountRemoteSource
            .updateAccountById(BuildConfig.ACCOUNT_ID, request)
            .transform { dto -> dto.asAccount() }
    }

}