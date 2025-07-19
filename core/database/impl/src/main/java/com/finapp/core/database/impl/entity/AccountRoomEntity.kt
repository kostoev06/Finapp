package com.finapp.core.database.impl.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "accounts"
)
data class AccountRoomEntity(
    @PrimaryKey
    val id: Long,
    val name: String,
    val balance: String,
    val currency: String,
    @ColumnInfo(name = "is_synced")
    val isSynced: Boolean = false
)
