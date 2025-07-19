package com.finapp.core.data.api.repository

import com.finapp.core.common.outcome.Outcome
import com.finapp.core.data.api.model.Category

interface CategoryRepository {
    suspend fun fetchCategories(): Outcome<List<Category>>
    suspend fun fetchCategoriesByType(isIncome: Boolean): Outcome<List<Category>>
}