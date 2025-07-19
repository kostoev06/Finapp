package com.finapp.core.database.impl.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.finapp.core.database.impl.entity.CategoryRoomEntity

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories")
    suspend fun getAll(): List<CategoryRoomEntity>

    @Query("SELECT * FROM categories WHERE id = :backendId LIMIT 1")
    suspend fun getByBackendId(backendId: Long): CategoryRoomEntity?

    @Query("SELECT * FROM categories WHERE is_income = :isIncome")
    suspend fun getByType(isIncome: Boolean): List<CategoryRoomEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: CategoryRoomEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<CategoryRoomEntity>)

    @Update
    suspend fun update(entity: CategoryRoomEntity)

    @Delete
    suspend fun delete(entity: CategoryRoomEntity)

    @Query("DELETE FROM categories")
    suspend fun deleteAll()
}