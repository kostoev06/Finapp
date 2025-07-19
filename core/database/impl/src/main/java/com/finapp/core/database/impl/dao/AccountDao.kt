package com.finapp.core.database.impl.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.finapp.core.database.impl.entity.AccountRoomEntity

@Dao
interface AccountDao {
    @Query("SELECT * FROM accounts WHERE id = :id LIMIT 1")
    suspend fun findById(id: Long): AccountRoomEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: AccountRoomEntity): Long

    @Update
    suspend fun update(entity: AccountRoomEntity)

    @Delete
    suspend fun delete(entity: AccountRoomEntity)

    @Query("DELETE FROM accounts")
    suspend fun deleteAll()
}