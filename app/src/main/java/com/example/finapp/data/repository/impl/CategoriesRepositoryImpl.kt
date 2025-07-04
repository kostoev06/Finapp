package com.example.finapp.data.repository.impl

import com.example.finapp.data.common.transform
import com.example.finapp.data.remote.CategoriesRemoteDataSource
import com.example.finapp.data.remote.dto.toDomain
import com.example.finapp.data.remote.retrofit.RetrofitCategoriesRemoteDataSource
import com.example.finapp.data.repository.CategoriesRepository


/**
 * Репозиторий категорий.
 */
class CategoriesRepositoryImpl(
    private val categoriesRemoteDataSource: CategoriesRemoteDataSource = RetrofitCategoriesRemoteDataSource()
) : CategoriesRepository {
    override suspend fun getCategories() =
        categoriesRemoteDataSource.fetchCategories()
            .transform { dtoList ->
                dtoList.map { it.toDomain() }
            }
}
