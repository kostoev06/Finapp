package com.example.finapp.domain

import java.math.BigDecimal

/**
 * Доменная модель одной записи статистики по счету.
 */
data class StatItem(
    val categoryId: Long,
    val categoryName: String,
    val emoji: String,
    val amount: BigDecimal
)
