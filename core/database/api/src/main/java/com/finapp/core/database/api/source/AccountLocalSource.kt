package com.finapp.core.database.api.source

import com.finapp.core.database.api.entity.AccountEntity

interface AccountLocalSource {
    suspend fun findById(backendId: Long): AccountEntity?
    suspend fun insert(entity: AccountEntity): Long
    suspend fun update(entity: AccountEntity)
    suspend fun delete(entity: AccountEntity)
    suspend fun deleteAll()
}