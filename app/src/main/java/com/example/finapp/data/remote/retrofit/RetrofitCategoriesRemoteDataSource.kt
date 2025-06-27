package com.example.finapp.data.remote.retrofit

import com.example.finapp.data.common.Outcome
import com.example.finapp.data.remote.AuthInterceptor
import com.example.finapp.data.remote.Backend
import com.example.finapp.data.remote.CategoriesRemoteDataSource
import com.example.finapp.data.remote.dto.CategoryDto
import com.example.finapp.data.remote.outcome.asRemoteResult
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private const val BASE_URL = Backend.SWAGGER_URL

private interface RetrofitCategoriesRemoteDataApi {
    @GET("categories")
    suspend fun getAllCategories(): Response<List<CategoryDto>>
}

class RetrofitCategoriesRemoteDataSource : CategoriesRemoteDataSource {

    private val remoteApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(
            OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor())
                .build()
        )
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RetrofitCategoriesRemoteDataApi::class.java)

    override suspend fun fetchAllCategories(): Outcome<List<CategoryDto>> {
        return try {
            remoteApi.getAllCategories()
                .asRemoteResult()
        } catch (t: Throwable) {
            Outcome.Exception(t)
        }
    }

}
