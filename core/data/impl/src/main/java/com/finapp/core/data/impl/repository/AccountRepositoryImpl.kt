package com.finapp.core.data.impl.repository

import com.finapp.core.common.outcome.Outcome
import com.finapp.core.common.outcome.transform
import com.finapp.core.data.api.model.Account
import com.finapp.core.data.api.repository.AccountIdProvider
import com.finapp.core.data.api.repository.AccountRepository
import com.finapp.core.data.impl.model.asAccount
import com.finapp.core.data.impl.model.asAccountEntity
import com.finapp.core.data.impl.model.asAccountUpdateRequest
import com.finapp.core.database.api.source.AccountLocalSource
import com.finapp.core.remote.api.model.AccountUpdateRequest
import com.finapp.core.remote.api.source.AccountRemoteSource
import jakarta.inject.Inject
import javax.inject.Singleton

/**
 * Репозиторий счета.
 */
@Singleton
class AccountRepositoryImpl @Inject constructor(
    private val accountRemoteSource: AccountRemoteSource,
    private val accountLocalSource: AccountLocalSource,
    private val accountIdProvider: AccountIdProvider
) : AccountRepository {
    override suspend fun fetchAccount(): Outcome<Account> =
        accountRemoteSource.fetchAccountById(accountIdProvider.get())
            .transform { dto -> dto.asAccount() }

    override suspend fun updateAccount(
        account: Account
    ): Outcome<Account> {
        return accountRemoteSource
            .updateAccountById(accountIdProvider.get(), account.asAccountUpdateRequest())
            .transform { dto -> dto.asAccount() }
    }

    override suspend fun getLocalAccount(): Account? =
        accountLocalSource.findById(accountIdProvider.get())?.asAccount()

    override suspend fun insertLocalAccount(account: Account): Long =
        accountLocalSource.insert(account.asAccountEntity())

    override suspend fun updateLocalAccount(account: Account) {
        accountLocalSource.update(account.asAccountEntity())
    }

    override suspend fun deleteLocalAccount(account: Account) {
        accountLocalSource.delete(account.asAccountEntity())
    }

    override suspend fun deleteAllLocalAccounts() {
        accountLocalSource.deleteAll()
    }

}