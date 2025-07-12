package com.finapp.core.remote.api.source

import com.finapp.core.common.outcome.Outcome
import com.finapp.core.remote.api.model.CategoryDto


interface CategoryRemoteSource {
    suspend fun fetchCategories(): Outcome<List<CategoryDto>>
    suspend fun fetchCategoriesByType(isIncome: Boolean): Outcome<List<CategoryDto>>
}