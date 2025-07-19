package com.finapp.core.database.api.entity

import java.math.BigDecimal
import java.time.LocalDateTime

data class TransactionEntity(
    val backendId: Long?,
    val accountBackendId: Long,
    val categoryBackendId: Long,
    val amount: BigDecimal,
    val transactionDate: LocalDateTime,
    val comment: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val isSynced: Boolean
)