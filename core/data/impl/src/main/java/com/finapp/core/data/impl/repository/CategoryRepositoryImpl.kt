package com.finapp.core.data.impl.repository

import com.finapp.core.common.outcome.Outcome
import com.finapp.core.common.outcome.transform
import com.finapp.core.data.api.model.Category
import com.finapp.core.data.api.repository.CategoryRepository
import com.finapp.core.data.impl.model.asCategory
import com.finapp.core.data.impl.model.asCategoryEntity
import com.finapp.core.database.api.source.CategoryLocalSource
import com.finapp.core.remote.api.source.CategoryRemoteSource
import jakarta.inject.Inject
import javax.inject.Singleton

/**
 * Репозиторий категорий.
 */
@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val categoryRemoteSource: CategoryRemoteSource,
    private val categoryLocalSource: CategoryLocalSource
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

    override suspend fun getAllLocalCategories(): List<Category> =
        categoryLocalSource.getAll().map { it.asCategory() }

    override suspend fun insertAllLocalCategories(list: List<Category>) {
        categoryLocalSource.insertAll(list.map { it.asCategoryEntity() })
    }

    override suspend fun deleteAllLocalCategories() {
        categoryLocalSource.deleteAll()
    }

    override suspend fun getLocalCategoryById(id: Long): Category? =
        categoryLocalSource.getById(id)?.asCategory()

    override suspend fun getLocalCategoriesByType(isIncome: Boolean): List<Category> =
        categoryLocalSource.getByType(isIncome).map { it.asCategory() }

    override suspend fun insertLocalCategory(category: Category): Long =
        categoryLocalSource.insert(category.asCategoryEntity())

    override suspend fun updateLocalCategory(category: Category) {
        categoryLocalSource.update(category.asCategoryEntity())
    }

    override suspend fun deleteLocalCategory(category: Category) {
        categoryLocalSource.delete(category.asCategoryEntity())
    }

}
