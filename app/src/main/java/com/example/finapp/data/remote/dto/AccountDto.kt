package com.example.finapp.data.remote.dto

import com.example.finapp.domain.Account
import java.time.LocalDateTime

data class AccountDto(
    val id: Long,
    val userId: Long,
    val name: String,
    val balance: String,
    val currency: String,
    val createdAt: String,
    val updatedAt: String
)

fun AccountDto.toDomain(): Account =
    Account(
        id = id,
        userId = userId,
        name = name,
        balance = balance.toBigDecimal(),
        currency = currency,
        createdAt = LocalDateTime.parse(createdAt),
        updatedAt = LocalDateTime.parse(updatedAt)
    )
