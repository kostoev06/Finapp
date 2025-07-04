package com.example.finapp.data.remote

import com.example.finapp.data.common.Outcome
import com.example.finapp.data.remote.dto.AccountDto
import com.example.finapp.data.remote.dto.AccountResponse
import com.example.finapp.data.remote.dto.AccountUpdateRequest

interface AccountRemoteDataSource {
    suspend fun fetchAccountById(accountId: Long): Outcome<AccountResponse>

    suspend fun updateAccountById(
        accountId: Long,
        update: AccountUpdateRequest
    ): Outcome<AccountDto>
}