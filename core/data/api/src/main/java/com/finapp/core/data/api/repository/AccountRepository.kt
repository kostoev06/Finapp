package com.finapp.core.data.api.repository

import com.finapp.core.common.outcome.Outcome
import com.finapp.core.data.api.model.Account

interface AccountRepository {
    suspend fun fetchAccount(): Outcome<Account>

    suspend fun updateAccount(
        name: String,
        balance: String,
        currency: String
    ): Outcome<Account>
}