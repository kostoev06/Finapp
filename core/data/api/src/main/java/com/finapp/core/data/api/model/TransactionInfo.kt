package com.finapp.core.data.api.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class TransactionInfo(
    val id: Long?,
    val accountId: Long,
    val categoryId: Long,
    val amount: BigDecimal,
    val transactionDate: LocalDateTime,
    val comment: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

fun Transaction.asTransactionInfo(): TransactionInfo =
    TransactionInfo(
        id = id,
        accountId = accountId,
        categoryId = category.id,
        amount = amount,
        transactionDate = transactionDate,
        comment = comment,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
