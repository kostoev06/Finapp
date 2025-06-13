package com.example.finapp.domain

import java.time.LocalDateTime
import java.time.OffsetDateTime

data class Transaction(
    val id: Long,
    val accountId: Long,
    val categoryId: Long,
    val amount: String,
    val transactionDate: LocalDateTime,
    val comment: String? = null,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
