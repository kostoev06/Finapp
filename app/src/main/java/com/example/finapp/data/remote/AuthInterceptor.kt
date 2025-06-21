package com.example.finapp.data.remote

import okhttp3.Interceptor
import okhttp3.Response as OkResponse

class AuthInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): OkResponse {
        val req = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(req)
    }
}