package com.finapp.core.data.api.model

/**
 * Доменная модель для категории.
 */
data class Category(
    val id: Long,
    val name: String,
    val emoji: String,
    val isIncome: Boolean
)
