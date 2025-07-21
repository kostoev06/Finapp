package com.finapp.core.database.impl.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.finapp.core.database.impl.entity.TransactionRoomEntity
import com.finapp.core.database.impl.relationships.TransactionAndCategoryRoom

@Dao
interface TransactionDao {
    @Transaction
    @Query(
        """
        SELECT * FROM transactions
        WHERE transaction_date_iso BETWEEN :startIso AND :endIso
        ORDER BY transaction_date_iso DESC
    """
    )
    suspend fun getByPeriodWithCategory(
        startIso: String,
        endIso: String
    ): List<TransactionAndCategoryRoom>

    @Transaction
    @Query(
        """
        SELECT * FROM transactions
        WHERE id = :id
    """
    )
    suspend fun getByIdWithCategory(id: Long): TransactionAndCategoryRoom?

    @Transaction
    @Query(
        """
        SELECT * FROM transactions
        WHERE id = :id AND is_synced = 1
    """
    )
    suspend fun getSyncedByIdWithCategory(id: Long): TransactionAndCategoryRoom?

    @Transaction
    @Query("SELECT * FROM transactions WHERE is_synced = 0 and is_new = 0")
    suspend fun getUnsyncedOld(): List<TransactionAndCategoryRoom>

    @Transaction
    @Query("SELECT * FROM transactions WHERE is_synced = 0 and is_new = 1")
    suspend fun getUnsyncedNew(): List<TransactionAndCategoryRoom>

    @Query("""
        SELECT substr(MIN(transaction_date_iso), 1, 10)
        FROM transactions
    """)
    suspend fun getOldestTransactionDate(): String?

    @Query("""
        SELECT substr(MAX(transaction_date_iso), 1, 10)
        FROM transactions
    """)
    suspend fun getNewestTransactionDate(): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: TransactionRoomEntity): Long

    @Update
    suspend fun update(entity: TransactionRoomEntity)

    @Query(
        """
        UPDATE transactions 
        SET is_synced = 1, id = :backendId 
        WHERE id = :id
    """
    )
    suspend fun markAsSynced(id: Long, backendId: Long)

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteByTableId(id: Long)

    @Query("DELETE FROM transactions")
    suspend fun deleteAll()
}