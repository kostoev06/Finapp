package com.finapp.core.data.api.model

import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * Доменная модель аккаунта.
 */
data class Account(
    val id: Long,
    val name: String,
    val balance: BigDecimal,
    val currency: CurrencyCode
)
