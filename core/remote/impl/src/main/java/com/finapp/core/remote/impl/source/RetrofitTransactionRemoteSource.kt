package com.finapp.core.remote.impl.source

import com.finapp.core.common.outcome.Outcome
import com.finapp.core.remote.api.model.TransactionDto
import com.finapp.core.remote.api.model.TransactionRequest
import com.finapp.core.remote.api.model.TransactionResponse
import com.finapp.core.remote.api.source.TransactionRemoteSource
import com.finapp.core.remote.impl.Backend
import com.finapp.core.remote.impl.AuthInterceptor
import com.finapp.core.remote.impl.outcome.OutcomeCallAdapterFactory
import jakarta.inject.Inject
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

private interface RetrofitTransactionRemoteApi {
    @GET("transactions/account/{accountId}/period")
    suspend fun fetchTransactionsByPeriod(
        @Path("accountId") accountId: Long,
        @Query("startDate") start: String? = null,
        @Query("endDate") end: String? = null
    ): Outcome<List<TransactionResponse>>

    @POST("transactions")
    suspend fun createTransaction(
        @Body body: TransactionRequest
    ): Outcome<TransactionDto>

    @PUT("transactions/{id}")
    suspend fun updateTransaction(
        @Path("id") id: Long,
        @Body body: TransactionRequest
    ): Outcome<TransactionResponse>

    @GET("transactions/{id}")
    suspend fun fetchTransactionById(
        @Path("id") id: Long
    ): Outcome<TransactionResponse>
}


/**
 * Источник данных для работы с транзакциями из удалённого API.
 */
@Singleton
class RetrofitTransactionRemoteSource @Inject constructor(
    retrofit: Retrofit
) : TransactionRemoteSource {

    private val remoteApi = retrofit
        .create(RetrofitTransactionRemoteApi::class.java)

    override suspend fun fetchTransactionsByPeriod(
        accountId: Long,
        startDate: String?,
        endDate: String?
    ): Outcome<List<TransactionResponse>> =
        remoteApi.fetchTransactionsByPeriod(accountId, startDate, endDate)

    override suspend fun createTransaction(request: TransactionRequest): Outcome<TransactionDto> =
        remoteApi.createTransaction(request)

    override suspend fun updateTransaction(
        id: Long,
        request: TransactionRequest
    ): Outcome<TransactionResponse> =
        remoteApi.updateTransaction(id, request)

    override suspend fun fetchTransactionById(id: Long): Outcome<TransactionResponse> =
        remoteApi.fetchTransactionById(id)
}
