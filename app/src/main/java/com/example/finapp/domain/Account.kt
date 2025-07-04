package com.example.finapp.domain

import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * Доменная модель аккаунта.
 */
data class Account(
    val id: Long,
    val name: String,
    val balance: BigDecimal,
    val currency: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
