package com.example.finapp.domain

import java.math.BigDecimal
import java.time.LocalDateTime


/**
 * Доменная модель для транзакции.
 */
data class Transaction(
    val id: Long,
    val accountId: Long,
    val category: Category,
    val amount: BigDecimal,
    val transactionDate: LocalDateTime,
    val comment: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
