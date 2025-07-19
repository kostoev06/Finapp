package com.finapp.core.database.api.entity

data class CategoryEntity(
    val id: Long,
    val name: String,
    val emoji: String,
    val isIncome: Boolean
)
