package com.example.finapp.data.remote.dto

import com.example.finapp.domain.Category

/**
 * DTO для категории, получаемый из API.
 */
data class CategoryDto(
    val id: Long,
    val name: String,
    val emoji: String,
    val isIncome: Boolean
)

fun CategoryDto.toDomain(): Category =
    Category(
        id = id,
        name = name,
        emoji = emoji,
        isIncome = isIncome
    )
