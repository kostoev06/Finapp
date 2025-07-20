package com.finapp.core.database.impl.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "transactions"
)
data class TransactionRoomEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    @ColumnInfo(name = "account_id")
    val accountId: Long,
    @ColumnInfo(name = "category_id")
    val categoryId: Long,
    val amount: String,
    @ColumnInfo(name = "transaction_date_iso")
    val transactionDateIso: String,
    val comment: String?,
    @ColumnInfo(name = "created_at_iso")
    val createdAtIso: String,
    @ColumnInfo(name = "updated_at_iso")
    val updatedAtIso: String,
    @ColumnInfo(name = "is_synced")
    val isSynced: Boolean = false,
    @ColumnInfo(name = "is_new")
    val isNew: Boolean = false
)
