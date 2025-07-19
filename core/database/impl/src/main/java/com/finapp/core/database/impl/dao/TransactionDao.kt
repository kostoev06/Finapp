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
    suspend fun getByLocalIdWithCategory(id: Long): TransactionAndCategoryRoom?

    @Transaction
    @Query(
        """
        SELECT * FROM transactions
        WHERE backend_id = :backendId
        LIMIT 1
    """
    )
    suspend fun getByBackendIdWithCategory(backendId: Long): TransactionAndCategoryRoom?

    @Transaction
    @Query("SELECT * FROM transactions WHERE is_synced = 0")
    suspend fun getUnsynced(): List<TransactionAndCategoryRoom>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: TransactionRoomEntity): Long

    @Update
    suspend fun update(entity: TransactionRoomEntity)

    @Query(
        """
        UPDATE transactions 
        SET is_synced = 1, backend_id = :backendId, updated_at_iso = :updatedAtIso 
        WHERE id = :id
    """
    )
    suspend fun markAsSynced(id: Long, backendId: Long, updatedAtIso: String)


    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteByTableId(id: Long)

    @Query("DELETE FROM transactions")
    suspend fun deleteAll()
}