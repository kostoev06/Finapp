package com.example.finapp.domain

/**
 * Доменная модель для состояния счета.
 */
data class AccountState(
    val id: Long,
    val name: String,
    val balance: String,
    val currency: String
)
