package com.finapp.core.database.impl.source

import com.finapp.core.database.api.entity.TransactionAndCategory
import com.finapp.core.database.api.entity.TransactionEntity
import com.finapp.core.database.api.source.TransactionLocalSource
import com.finapp.core.database.impl.dao.TransactionDao
import com.finapp.core.database.impl.mapper.toEntity
import com.finapp.core.database.impl.mapper.toRoom
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomTransactionLocalSource @Inject constructor(
    private val dao: TransactionDao
) : TransactionLocalSource {
    override suspend fun getByLocalId(id: Long): TransactionAndCategory? =
        dao.getByLocalIdWithCategory(id)?.toEntity()

    override suspend fun getByBackendId(backendId: Long): TransactionAndCategory? =
        dao.getByBackendIdWithCategory(backendId)?.toEntity()

    override suspend fun getByPeriod(startIso: String, endIso: String): List<TransactionAndCategory> =
        dao.getByPeriodWithCategory(startIso, endIso)
            .map { it.toEntity() }

    override suspend fun getUnsynced(): List<TransactionAndCategory> =
        dao.getUnsynced().map { it.toEntity() }

    override suspend fun insert(entity: TransactionEntity): Long =
        dao.insert(entity.toRoom())

    override suspend fun update(entity: TransactionEntity) =
        dao.update(entity.toRoom())

    override suspend fun markSynced(tableId: Long, backendId: Long, updatedAtIso: String) =
        dao.markAsSynced(tableId, backendId, updatedAtIso)

    override suspend fun delete(tableId: Long) =
        dao.deleteByTableId(tableId)

    override suspend fun deleteAll() = dao.deleteAll()
}