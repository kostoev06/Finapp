package com.finapp.core.remote.impl.source

import com.finapp.core.common.outcome.Outcome
import com.finapp.core.remote.api.model.AccountDto
import com.finapp.core.remote.api.model.AccountResponse
import com.finapp.core.remote.api.model.AccountUpdateRequest
import com.finapp.core.remote.api.source.AccountRemoteSource
import com.finapp.core.remote.impl.AuthInterceptor
import com.finapp.core.remote.impl.Backend
import com.finapp.core.remote.impl.outcome.OutcomeCallAdapterFactory
import jakarta.inject.Inject
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import javax.inject.Singleton

private interface RetrofitAccountRemoteApi {
    @GET("accounts/{id}")
    suspend fun fetchAccountById(
        @Path("id") accountId: Long
    ): Outcome<AccountResponse>

    @PUT("accounts/{id}")
    suspend fun updateAccountById(
        @Path("id") accountId: Long,
        @Body update: AccountUpdateRequest
    ): Outcome<AccountDto>
}

/**
 * Источник данных для работы со счетом из удалённого API.
 */
@Singleton
class RetrofitAccountRemoteSource @Inject constructor(
    retrofit: Retrofit
) : AccountRemoteSource {

    private val remoteApi = retrofit
        .create(RetrofitAccountRemoteApi::class.java)

    override suspend fun fetchAccountById(accountId: Long): Outcome<AccountResponse> =
        remoteApi.fetchAccountById(accountId)

    override suspend fun updateAccountById(
        accountId: Long,
        update: AccountUpdateRequest
    ): Outcome<AccountDto> =
        remoteApi.updateAccountById(accountId, update)

}