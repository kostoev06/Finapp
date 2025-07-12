package com.finapp.core.remote.api.model

/**
 * DTO ответа /accounts/{id}
 */
data class AccountResponse(
    val id: Long,
    val name: String,
    val balance: String,
    val currency: String,
    val incomeStats: List<StatItemDto>,
    val expenseStats: List<StatItemDto>,
    val createdAt: String,
    val updatedAt: String
)
