package com.example.finapp.data.remote

import com.example.finapp.data.remote.dto.CategoryDto
import com.example.finapp.data.remote.dto.TransactionResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("transactions/account/{accountId}/period")
    suspend fun getTransactionsByPeriod(
        @Path("accountId") accountId: Long,
        @Query("startDate") start: String? = null,
        @Query("endDate") end: String? = null
    ): Response<List<TransactionResponse>>

    @GET("categories")
    suspend fun getAllCategories(): Response<List<CategoryDto>>

}