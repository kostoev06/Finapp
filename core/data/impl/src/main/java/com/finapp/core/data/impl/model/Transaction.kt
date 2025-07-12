package com.finapp.core.data.impl.model

import com.finapp.core.data.api.model.Category
import com.finapp.core.data.api.model.Transaction
import com.finapp.core.data.api.model.TransactionBrief
import com.finapp.core.remote.api.model.TransactionDto
import com.finapp.core.remote.api.model.TransactionResponse
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun TransactionDto.asTransactionBrief(): TransactionBrief = TransactionBrief(
    id = id,
    accountId = accountId,
    categoryId = categoryId,
    amount = amount.toBigDecimal(),
    transactionDate = LocalDateTime.parse(
        transactionDate,
        DateTimeFormatter.ISO_OFFSET_DATE_TIME
    ),
    comment = comment
)

fun TransactionResponse.asTransaction(): Transaction =
    Transaction(
        id = id,
        accountId = account.id,
        category = Category(
            category.id,
            category.name,
            category.emoji,
            category.isIncome
        ),
        amount = amount.toBigDecimal(),
        transactionDate = LocalDateTime.parse(
            transactionDate,
            DateTimeFormatter.ISO_OFFSET_DATE_TIME
        ),
        comment = comment,
        createdAt = LocalDateTime.parse(
            createdAt,
            DateTimeFormatter.ISO_OFFSET_DATE_TIME
        ),
        updatedAt = LocalDateTime.parse(
            updatedAt,
            DateTimeFormatter.ISO_OFFSET_DATE_TIME
        )
    )
