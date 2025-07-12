package com.finapp.core.remote.api.model

/**
 * DTO одной записи статистики (доходы или расходы) из ответа AccountResponse.
 */
data class StatItemDto(
    val categoryId: Long,
    val categoryName: String,
    val emoji: String,
    val amount: String
)
