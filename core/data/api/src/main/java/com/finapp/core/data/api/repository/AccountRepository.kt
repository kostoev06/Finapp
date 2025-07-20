package com.finapp.core.data.api.repository

import com.finapp.core.common.outcome.Outcome
import com.finapp.core.data.api.model.Account

interface AccountRepository {
    suspend fun fetchAccount(): Outcome<Account>
    suspend fun updateAccount(
        account: Account
    ): Outcome<Account>

    suspend fun getLocalAccount(): Account?
    suspend fun insertLocalAccount(account: Account): Long
    suspend fun updateLocalAccount(account: Account)
    suspend fun deleteLocalAccount(account: Account)
    suspend fun deleteAllLocalAccounts()
}