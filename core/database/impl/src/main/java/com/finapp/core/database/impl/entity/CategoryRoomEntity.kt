package com.finapp.core.database.impl.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "categories"
)
data class CategoryRoomEntity(
    @PrimaryKey
    val id: Long,
    val name: String,
    val emoji: String,
    @ColumnInfo(name = "is_income")
    val isIncome: Boolean
)
