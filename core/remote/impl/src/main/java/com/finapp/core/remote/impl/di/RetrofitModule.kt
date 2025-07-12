package com.finapp.core.remote.impl.di

import com.finapp.core.remote.impl.AuthInterceptor
import com.finapp.core.remote.impl.Backend
import com.finapp.core.remote.impl.outcome.OutcomeCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val BASE_URL = Backend.SWAGGER_URL

@Module
class RetrofitModule {
    companion object {
        @Provides
        @Singleton
        @JvmStatic
        fun provideRetrofit(): Retrofit =
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(
                    OkHttpClient.Builder()
                        .addInterceptor(AuthInterceptor())
                        .build()
                )
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(OutcomeCallAdapterFactory.create())
                .build()
    }
}