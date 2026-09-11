package com.noniboy.struja.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.noniboy.struja.data.db.entity.MeterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MeterDao {
    @Query("SELECT * FROM meters ORDER BY createdAt DESC")
    fun getAll(): Flow<List<MeterEntity>>

    @Query("SELECT * FROM meters ORDER BY createdAt DESC")
    suspend fun getAllList(): List<MeterEntity>

    @Query("SELECT * FROM meters WHERE id = :id")
    suspend fun getById(id: String): MeterEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(meter: MeterEntity)

    @Delete
    suspend fun delete(meter: MeterEntity)

    @Query("DELETE FROM meters WHERE id = :id")
    suspend fun deleteById(id: String)
}
