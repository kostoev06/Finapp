package com.finapp.core.database.api.source

import com.finapp.core.database.api.entity.TransactionAndCategory
import com.finapp.core.database.api.entity.TransactionEntity

interface TransactionLocalSource {
    suspend fun getByLocalId(id: Long): TransactionAndCategory?
    suspend fun getByBackendId(backendId: Long): TransactionAndCategory?
    suspend fun getByPeriod(startIso: String, endIso: String): List<TransactionAndCategory>

    suspend fun getUnsynced(): List<TransactionAndCategory>
    suspend fun insert(entity: TransactionEntity): Long
    suspend fun update(entity: TransactionEntity)
    suspend fun markSynced(tableId: Long, backendId: Long, updatedAtIso: String)
    suspend fun delete(tableId: Long)
    suspend fun deleteAll()
}