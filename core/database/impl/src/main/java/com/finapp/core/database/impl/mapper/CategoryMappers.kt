package com.finapp.core.database.impl.mapper

import com.finapp.core.database.api.entity.CategoryEntity
import com.finapp.core.database.impl.entity.CategoryRoomEntity

fun CategoryRoomEntity.toEntity(): CategoryEntity =
    CategoryEntity(
        id = id,
        name = name,
        emoji = emoji,
        isIncome = isIncome
    )

fun CategoryEntity.toRoom(): CategoryRoomEntity =
    CategoryRoomEntity(
        id = id,
        name = name,
        emoji = emoji,
        isIncome = isIncome
    )