package com.finapp.core.remote.api.model

/**
 * DTO для категории, получаемый из API.
 */
data class CategoryDto(
    val id: Long,
    val name: String,
    val emoji: String,
    val isIncome: Boolean
)
