package com.finapp.core.remote.api.model

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
