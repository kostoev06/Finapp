package com.finapp.core.data.api.repository

import com.finapp.core.common.outcome.Outcome
import com.finapp.core.data.api.model.Category

interface CategoryRepository {
    suspend fun getCategories(): Outcome<List<Category>>
    suspend fun getCategoriesByType(isIncome: Boolean): Outcome<List<Category>>
}