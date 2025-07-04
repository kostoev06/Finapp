package com.example.finapp.data.repository.impl

import com.example.finapp.BuildConfig
import com.example.finapp.data.common.Outcome
import com.example.finapp.data.common.transform
import com.example.finapp.data.remote.AccountRemoteDataSource
import com.example.finapp.data.remote.dto.AccountUpdateRequest
import com.example.finapp.data.remote.dto.toDomain
import com.example.finapp.data.remote.retrofit.RetrofitAccountRemoteDataSource
import com.example.finapp.data.repository.AccountRepository
import com.example.finapp.domain.Account

/**
 * Репозиторий счета.
 */
class AccountRepositoryImpl(
    private val accountRemoteDataSource: AccountRemoteDataSource = RetrofitAccountRemoteDataSource()
) : AccountRepository {
    override suspend fun getAccount(): Outcome<Account> =
        accountRemoteDataSource.fetchAccountById(BuildConfig.ACCOUNT_ID)
            .transform { dto -> dto.toDomain() }

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
        return accountRemoteDataSource
            .updateAccountById(BuildConfig.ACCOUNT_ID, request)
            .transform { dto -> dto.toDomain() }
    }

}
