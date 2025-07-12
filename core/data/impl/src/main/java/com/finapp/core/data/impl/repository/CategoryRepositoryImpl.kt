package com.finapp.core.data.impl.repository

import com.finapp.core.common.outcome.Outcome
import com.finapp.core.common.outcome.transform
import com.finapp.core.data.api.model.Category
import com.finapp.core.data.api.repository.CategoryRepository
import com.finapp.core.data.impl.model.asCategory
import com.finapp.core.remote.api.source.CategoryRemoteSource
import com.finapp.core.remote.impl.source.RetrofitCategoryRemoteSource
import jakarta.inject.Inject
import javax.inject.Singleton

/**
 * Репозиторий категорий.
 */
@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val categoryRemoteSource: CategoryRemoteSource
) : CategoryRepository {
    override suspend fun getCategories() =
        categoryRemoteSource.fetchCategories()
            .transform { dtoList ->
                dtoList.map { it.asCategory() }
            }

    override suspend fun getCategoriesByType(isIncome: Boolean): Outcome<List<Category>> =
        categoryRemoteSource.fetchCategoriesByType(isIncome)
            .transform { dtoList ->
                dtoList.map { it.asCategory() }
            }

}
