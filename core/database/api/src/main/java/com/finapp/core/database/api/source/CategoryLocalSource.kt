package com.finapp.core.database.api.source

import com.finapp.core.database.api.entity.CategoryEntity

interface CategoryLocalSource {
    suspend fun getAll(): List<CategoryEntity>
    suspend fun insertAll(list: List<CategoryEntity>)
    suspend fun deleteAll()
    suspend fun getById(id: Long): CategoryEntity?
    suspend fun getByType(isIncome: Boolean): List<CategoryEntity>
    suspend fun insert(entity: CategoryEntity): Long
    suspend fun update(entity: CategoryEntity)
    suspend fun delete(entity: CategoryEntity)
}