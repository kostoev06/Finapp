package com.finapp.core.data.impl.model

import com.finapp.core.data.api.model.Category
import com.finapp.core.database.api.entity.CategoryEntity
import com.finapp.core.remote.api.model.CategoryDto

fun CategoryDto.asCategory(): Category =
    Category(
        id = id,
        name = name,
        emoji = emoji,
        isIncome = isIncome
    )

fun CategoryEntity.asCategory(): Category =
    Category(
        id = id,
        name = name,
        emoji = emoji,
        isIncome = isIncome
    )

fun Category.asCategoryEntity(): CategoryEntity =
    CategoryEntity(
        id = id,
        name = name,
        emoji = emoji,
        isIncome = isIncome
    )