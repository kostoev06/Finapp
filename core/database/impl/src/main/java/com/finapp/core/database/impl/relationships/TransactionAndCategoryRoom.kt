package com.finapp.core.database.impl.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.finapp.core.database.impl.entity.CategoryRoomEntity
import com.finapp.core.database.impl.entity.TransactionRoomEntity

data class TransactionAndCategoryRoom(
    @Embedded
    val transaction: TransactionRoomEntity,
    @Relation(
        parentColumn = "category_id",
        entityColumn = "id"
    )
    val category: CategoryRoomEntity
)
