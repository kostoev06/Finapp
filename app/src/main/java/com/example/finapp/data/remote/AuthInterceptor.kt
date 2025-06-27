package com.example.finapp.data.remote

import okhttp3.Interceptor
import okhttp3.Response as OkResponse
import com.example.finapp.BuildConfig

class AuthInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): OkResponse {
        val req = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer ${BuildConfig.API_KEY}")
            .build()
        return chain.proceed(req)
    }
}
