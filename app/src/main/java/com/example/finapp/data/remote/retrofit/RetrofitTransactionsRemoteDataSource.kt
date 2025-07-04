package com.example.finapp.data.remote.retrofit

import com.example.finapp.data.common.Outcome
import com.example.finapp.data.remote.AuthInterceptor
import com.example.finapp.data.remote.Backend
import com.example.finapp.data.remote.TransactionsRemoteDataSource
import com.example.finapp.data.remote.dto.TransactionResponse
import com.example.finapp.data.remote.outcome.OutcomeCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = Backend.SWAGGER_URL

private interface RetrofitTransactionsRemoteDataApi {
    @GET("transactions/account/{accountId}/period")
    suspend fun fetchTransactionsByPeriod(
        @Path("accountId") accountId: Long,
        @Query("startDate") start: String? = null,
        @Query("endDate") end: String? = null
    ): Outcome<List<TransactionResponse>>
}


/**
 * Источник данных для работы с транзакциями из удалённого API.
 */
class RetrofitTransactionsRemoteDataSource : TransactionsRemoteDataSource {

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
        .create(RetrofitTransactionsRemoteDataApi::class.java)

    override suspend fun fetchTransactionsByPeriod(
        accountId: Long,
        startDate: String?,
        endDate: String?
    ): Outcome<List<TransactionResponse>> =
        remoteApi.fetchTransactionsByPeriod(accountId, startDate, endDate)

}
