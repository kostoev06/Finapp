package com.example.finapp.data.remote.dto

import com.example.finapp.domain.StatItem

/**
 * DTO одной записи статистики (доходы или расходы) из ответа AccountResponse.
 */
data class StatItemDto(
    val categoryId: Long,
    val categoryName: String,
    val emoji: String,
    val amount: String
)

fun StatItemDto.toDomain(): StatItem =
    StatItem(
        categoryId = categoryId,
        categoryName = categoryName,
        emoji = emoji,
        amount = amount.toBigDecimal()
    )
