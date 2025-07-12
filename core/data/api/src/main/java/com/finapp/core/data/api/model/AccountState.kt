package com.finapp.core.data.api.model

/**
 * Доменная модель для состояния счета.
 */
data class AccountState(
    val id: Long,
    val name: String,
    val balance: String,
    val currency: CurrencyCode
)
