package com.example.finapp.data.repository

import com.example.finapp.data.common.Outcome
import com.example.finapp.domain.Category

interface CategoriesRepository {
    suspend fun getAllCategories(): Outcome<List<Category>>
}
