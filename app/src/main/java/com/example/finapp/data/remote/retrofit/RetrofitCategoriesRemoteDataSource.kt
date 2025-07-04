package com.example.finapp.data.remote.retrofit

import com.example.finapp.data.common.Outcome
import com.example.finapp.data.remote.AuthInterceptor
import com.example.finapp.data.remote.Backend
import com.example.finapp.data.remote.CategoriesRemoteDataSource
import com.example.finapp.data.remote.dto.CategoryDto
import com.example.finapp.data.remote.outcome.OutcomeCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private const val BASE_URL = Backend.SWAGGER_URL

private interface RetrofitCategoriesRemoteDataApi {
    @GET("categories")
    suspend fun fetchCategories(): Outcome<List<CategoryDto>>
}


/**
 * Источник данных для работы с категориями из удалённого API.
 */
class RetrofitCategoriesRemoteDataSource : CategoriesRemoteDataSource {

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
        .create(RetrofitCategoriesRemoteDataApi::class.java)

    override suspend fun fetchCategories(): Outcome<List<CategoryDto>> =
        remoteApi.fetchCategories()

}
