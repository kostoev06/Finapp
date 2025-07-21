package com.finapp.core.database.api.source

import com.finapp.core.database.api.entity.TransactionAndCategory
import com.finapp.core.database.api.entity.TransactionEntity

interface TransactionLocalSource {
    suspend fun getById(id: Long): TransactionAndCategory?
    suspend fun getSyncedById(id: Long): TransactionAndCategory?
    suspend fun getByPeriod(startIso: String, endIso: String): List<TransactionAndCategory>
    suspend fun getUnsyncedOld(): List<TransactionAndCategory>
    suspend fun getUnsyncedNew(): List<TransactionAndCategory>
    suspend fun getOldestDate(): String?
    suspend fun getNewestDate(): String?
    suspend fun insert(entity: TransactionEntity): Long
    suspend fun update(entity: TransactionEntity)
    suspend fun markSynced(tableId: Long, backendId: Long)
    suspend fun delete(tableId: Long)
    suspend fun deleteAll()
}