package com.example.finapp.data.remote.dto

import com.example.finapp.domain.Account
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * DTO для полной информации об аккаунте, получаемой из API.
 */
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
        name = name,
        balance = balance.toBigDecimal(),
        currency = currency,
        createdAt = LocalDateTime.parse(
            createdAt,
            DateTimeFormatter.ISO_OFFSET_DATE_TIME
        ),
        updatedAt = LocalDateTime.parse(
            updatedAt,
            DateTimeFormatter.ISO_OFFSET_DATE_TIME
        )
    )
