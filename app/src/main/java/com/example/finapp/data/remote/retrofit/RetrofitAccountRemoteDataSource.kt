package com.example.finapp.data.remote.retrofit

import com.example.finapp.data.common.Outcome
import com.example.finapp.data.remote.AccountRemoteDataSource
import com.example.finapp.data.remote.AuthInterceptor
import com.example.finapp.data.remote.Backend
import com.example.finapp.data.remote.dto.AccountDto
import com.example.finapp.data.remote.dto.AccountResponse
import com.example.finapp.data.remote.dto.AccountUpdateRequest
import com.example.finapp.data.remote.outcome.OutcomeCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

private const val BASE_URL = Backend.SWAGGER_URL

private interface RetrofitAccountRemoteDataApi {
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
class RetrofitAccountRemoteDataSource : AccountRemoteDataSource{

    private val remoteApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(
            OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor())
                .build()
        )
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(OutcomeCallAdapterFactory.create())
        .build()
        .create(RetrofitAccountRemoteDataApi::class.java)

    override suspend fun fetchAccountById(accountId: Long): Outcome<AccountResponse> =
        remoteApi.fetchAccountById(accountId)

    override suspend fun updateAccountById(
        accountId: Long,
        update: AccountUpdateRequest
    ): Outcome<AccountDto> =
        remoteApi.updateAccountById(accountId, update)

}
