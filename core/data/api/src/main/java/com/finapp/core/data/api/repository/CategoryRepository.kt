package com.finapp.core.data.api.repository

import com.finapp.core.common.outcome.Outcome
import com.finapp.core.data.api.model.Category

interface CategoryRepository {
    suspend fun fetchCategories(): Outcome<List<Category>>
    suspend fun fetchCategoriesByType(isIncome: Boolean): Outcome<List<Category>>

    suspend fun getAllLocalCategories(): List<Category>
    suspend fun insertAllLocalCategories(list: List<Category>)
    suspend fun deleteAllLocalCategories()
    suspend fun getLocalCategoryById(id: Long): Category?
    suspend fun getLocalCategoriesByType(isIncome: Boolean): List<Category>
    suspend fun insertLocalCategory(category: Category): Long
    suspend fun updateLocalCategory(category: Category)
    suspend fun deleteLocalCategory(category: Category)
}