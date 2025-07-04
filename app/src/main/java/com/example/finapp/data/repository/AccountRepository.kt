package com.example.finapp.data.repository

import com.example.finapp.data.common.Outcome
import com.example.finapp.domain.Account

interface AccountRepository {
    suspend fun getAccount(): Outcome<Account>

    suspend fun updateAccount(
        name: String,
        balance: String,
        currency: String
    ): Outcome<Account>
}