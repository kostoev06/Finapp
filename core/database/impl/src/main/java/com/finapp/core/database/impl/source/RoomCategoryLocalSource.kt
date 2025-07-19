package com.finapp.core.database.impl.source

import com.finapp.core.database.api.entity.CategoryEntity
import com.finapp.core.database.api.source.CategoryLocalSource
import com.finapp.core.database.impl.dao.CategoryDao
import com.finapp.core.database.impl.mapper.toEntity
import com.finapp.core.database.impl.mapper.toRoom
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomCategoryLocalSource @Inject constructor(
    private val dao: CategoryDao
) : CategoryLocalSource {

    override suspend fun getAll(): List<CategoryEntity> =
        dao.getAll().map { it.toEntity() }

    override suspend fun getByBackendId(backendId: Long): CategoryEntity? =
        dao.getByBackendId(backendId)?.toEntity()

    override suspend fun getByType(isIncome: Boolean): List<CategoryEntity> =
        dao.getByType(isIncome).map { it.toEntity() }

    override suspend fun insert(entity: CategoryEntity): Long =
        dao.insert(entity.toRoom())

    override suspend fun insertAll(list: List<CategoryEntity>) =
        dao.insertAll(list.map { it.toRoom() })

    override suspend fun update(entity: CategoryEntity) {
        dao.update(entity.toRoom())
    }

    override suspend fun delete(entity: CategoryEntity) {
        dao.delete(entity.toRoom())
    }

    override suspend fun deleteAll() =
        dao.deleteAll()
}
