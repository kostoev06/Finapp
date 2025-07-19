package com.finapp.core.database.impl

import androidx.room.Database
import androidx.room.RoomDatabase
import com.finapp.core.database.impl.dao.AccountDao
import com.finapp.core.database.impl.dao.CategoryDao
import com.finapp.core.database.impl.dao.TransactionDao
import com.finapp.core.database.impl.entity.TransactionRoomEntity
import com.finapp.core.database.impl.entity.AccountRoomEntity
import com.finapp.core.database.impl.entity.CategoryRoomEntity

@Database(
    entities = [
        TransactionRoomEntity::class,
        CategoryRoomEntity::class,
        AccountRoomEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FinappDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
    abstract fun accountDao(): AccountDao
}