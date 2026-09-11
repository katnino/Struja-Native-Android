package com.noniboy.struja.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.noniboy.struja.data.db.entity.ReadingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReadingDao {
    @Query("SELECT * FROM readings WHERE meterId = :meterId ORDER BY recordedAt ASC")
    fun getByMeterId(meterId: String): Flow<List<ReadingEntity>>

    @Query("SELECT * FROM readings WHERE meterId = :meterId ORDER BY recordedAt DESC LIMIT 1")
    suspend fun getLatestByMeterId(meterId: String): ReadingEntity?

    @Query("SELECT * FROM readings WHERE meterId = :meterId ORDER BY recordedAt ASC")
    suspend fun getByMeterIdList(meterId: String): List<ReadingEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reading: ReadingEntity)
}
