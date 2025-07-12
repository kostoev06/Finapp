package com.finapp.core.remote.api.model

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
