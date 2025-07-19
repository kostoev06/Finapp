package com.finapp.core.database.impl.source

import com.finapp.core.database.api.entity.AccountEntity
import com.finapp.core.database.api.source.AccountLocalSource
import com.finapp.core.database.impl.dao.AccountDao
import com.finapp.core.database.impl.mapper.toEntity
import com.finapp.core.database.impl.mapper.toRoom
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomAccountLocalSource @Inject constructor(
    private val dao: AccountDao
) : AccountLocalSource {
    override suspend fun findById(backendId: Long): AccountEntity? =
        dao.findById(backendId)?.toEntity()

    override suspend fun insert(entity: AccountEntity): Long =
        dao.insert(entity.toRoom())

    override suspend fun update(entity: AccountEntity) =
        dao.update(entity.toRoom())

    override suspend fun delete(entity: AccountEntity) {
        dao.delete(entity.toRoom())
    }

    override suspend fun deleteAll() = dao.deleteAll()
}