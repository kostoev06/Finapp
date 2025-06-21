package com.example.finapp.domain

import java.math.BigDecimal
import java.time.LocalDateTime

data class Account(
    val id: Long,
    val userId: Long,
    val name: String,
    val balance: BigDecimal,
    val currency: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
