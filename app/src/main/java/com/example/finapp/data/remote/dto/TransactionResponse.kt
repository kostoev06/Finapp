package com.example.finapp.data.remote.dto

import com.example.finapp.domain.Category
import com.example.finapp.domain.Transaction
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


/**
 * DTO для ответа транзакций из API.
 */
data class TransactionResponse(
    val id: Long,
    val account: AccountBriefDto,
    val category: CategoryDto,
    val amount: String,
    val transactionDate: String,
    val comment: String?,
    val createdAt: String,
    val updatedAt: String
)

fun TransactionResponse.toDomain(): Transaction =
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
        transactionDate  = LocalDateTime.parse(
            transactionDate,
            DateTimeFormatter.ISO_OFFSET_DATE_TIME
        ),
        comment = comment,
        createdAt = LocalDateTime.parse(
            transactionDate,
            DateTimeFormatter.ISO_OFFSET_DATE_TIME
        ),
        updatedAt = LocalDateTime.parse(
            transactionDate,
            DateTimeFormatter.ISO_OFFSET_DATE_TIME
        )
    )
