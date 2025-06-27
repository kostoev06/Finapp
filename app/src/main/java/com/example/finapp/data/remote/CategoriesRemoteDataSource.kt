package com.example.finapp.data.remote

import com.example.finapp.data.common.Outcome
import com.example.finapp.data.remote.dto.CategoryDto

interface CategoriesRemoteDataSource {
    suspend fun fetchAllCategories(): Outcome<List<CategoryDto>>
}
