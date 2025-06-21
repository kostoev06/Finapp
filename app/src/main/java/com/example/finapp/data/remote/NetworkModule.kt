package com.example.finapp.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {
    private const val BASE_URL = "https://shmr-finance.ru/api/v1/"
    private const val TOKEN = "YOUR_API_TOKEN"

    val apiService: ApiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(
            OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(TOKEN))
                .build()
        )
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
}