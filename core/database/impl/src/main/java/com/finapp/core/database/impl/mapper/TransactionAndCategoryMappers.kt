package com.finapp.core.database.impl.mapper

import com.finapp.core.database.api.entity.TransactionAndCategory
import com.finapp.core.database.impl.relationships.TransactionAndCategoryRoom

fun TransactionAndCategoryRoom.toEntity(): TransactionAndCategory =
    TransactionAndCategory(
        transaction = transaction.toEntity(),
        category = category.toEntity()
    )

fun TransactionAndCategory.toRoom(): TransactionAndCategoryRoom =
    TransactionAndCategoryRoom(
        transaction = transaction.toRoom(),
        category = category.toRoom()
    )
