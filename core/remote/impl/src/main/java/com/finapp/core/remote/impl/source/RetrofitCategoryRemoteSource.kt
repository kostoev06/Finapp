package com.finapp.core.remote.impl.source

import com.finapp.core.common.outcome.Outcome
import com.finapp.core.remote.api.model.CategoryDto
import com.finapp.core.remote.api.source.CategoryRemoteSource
import com.finapp.core.remote.impl.AuthInterceptor
import com.finapp.core.remote.impl.Backend
import com.finapp.core.remote.impl.outcome.OutcomeCallAdapterFactory
import jakarta.inject.Inject
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Singleton

private interface RetrofitCategoryRemoteApi {
    @GET("categories")
    suspend fun fetchCategories(): Outcome<List<CategoryDto>>

    @GET("categories/type/{isIncome}")
    suspend fun fetchCategoriesByType(
        @Path("isIncome") isIncome: Boolean
    ): Outcome<List<CategoryDto>>
}


/**
 * Источник данных для работы с категориями из удалённого API.
 */
@Singleton
class RetrofitCategoryRemoteSource @Inject constructor(
    retrofit: Retrofit
) : CategoryRemoteSource {

    private val remoteApi = retrofit
        .create(RetrofitCategoryRemoteApi::class.java)

    override suspend fun fetchCategories(): Outcome<List<CategoryDto>> =
        remoteApi.fetchCategories()

    override suspend fun fetchCategoriesByType(isIncome: Boolean): Outcome<List<CategoryDto>> =
        remoteApi.fetchCategoriesByType(isIncome)

}