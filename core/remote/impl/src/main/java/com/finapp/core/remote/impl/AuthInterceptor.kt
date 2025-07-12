package com.finapp.core.remote.impl

import okhttp3.Interceptor
import okhttp3.Response as OkResponse

/**
 * Interceptor для Retrofit: добавляет токен аутентификации ко всем запросам.
 */
class AuthInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): OkResponse {
        val req = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer ${BuildConfig.API_KEY}")
            .build()
        return chain.proceed(req)
    }
}