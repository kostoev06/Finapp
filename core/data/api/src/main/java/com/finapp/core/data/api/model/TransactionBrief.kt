package com.finapp.core.data.api.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class TransactionBrief(
    val id: Long?,
    val categoryId: Long,
    val amount: BigDecimal,
    val transactionDate: LocalDateTime,
    val comment: String?
)

fun Transaction.asTransactionBrief(): TransactionBrief =
    TransactionBrief(
        id = id,
        categoryId = category.id,
        amount = amount,
        transactionDate = transactionDate,
        comment = comment
    )
