package com.finapp.core.data.impl.repository

import com.finapp.core.common.outcome.Outcome
import com.finapp.core.common.outcome.transform
import com.finapp.core.data.api.model.Category
import com.finapp.core.data.api.repository.CategoryRepository
import com.finapp.core.data.impl.model.asCategory
import com.finapp.core.remote.api.source.CategoryRemoteSource
import jakarta.inject.Inject
import javax.inject.Singleton

/**
 * Репозиторий категорий.
 */
@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val categoryRemoteSource: CategoryRemoteSource
) : CategoryRepository {
    override suspend fun fetchCategories() =
        categoryRemoteSource.fetchCategories()
            .transform { dtoList ->
                dtoList.map { it.asCategory() }
            }

    override suspend fun fetchCategoriesByType(isIncome: Boolean): Outcome<List<Category>> =
        categoryRemoteSource.fetchCategoriesByType(isIncome)
            .transform { dtoList ->
                dtoList.map { it.asCategory() }
            }

}
