package com.finapp.core.remote.api.source

import com.finapp.core.common.outcome.Outcome
import com.finapp.core.remote.api.model.AccountDto
import com.finapp.core.remote.api.model.AccountResponse
import com.finapp.core.remote.api.model.AccountUpdateRequest


interface AccountRemoteSource {
    suspend fun fetchAccountById(accountId: Long): Outcome<AccountResponse>

    suspend fun updateAccountById(
        accountId: Long,
        update: AccountUpdateRequest
    ): Outcome<AccountDto>
}