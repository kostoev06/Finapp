package com.example.finapp.data.repository.impl

import com.example.finapp.data.common.transform
import com.example.finapp.data.remote.RemoteDataSource
import com.example.finapp.data.remote.dto.toDomain
import com.example.finapp.data.repository.CategoriesRepository

class CategoriesRepositoryImpl(
    private val remote: RemoteDataSource = RemoteDataSource()
) : CategoriesRepository {
    override suspend fun getAllCategories() =
        remote.fetchAllCategories()
            .transform { dtoList ->
                dtoList.map { it.toDomain() }
            }
}