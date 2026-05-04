package com.finapp.core.remote.impl

import okhttp3.Interceptor
import javax.inject.Inject
import javax.inject.Singleton
import okhttp3.Response as OkResponse

/**
 * Interceptor для Retrofit: добавляет токен аутентификации ко всем запросам.
 */
@Singleton
class AuthInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): OkResponse {
        val req = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer ${BuildConfig.API_KEY}")
            .build()
        return chain.proceed(req)
    }
}
