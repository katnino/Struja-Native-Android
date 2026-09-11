package com.noniboy.struja.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.noniboy.struja.data.db.entity.BillEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BillDao {
    @Query("SELECT * FROM bills WHERE meterId = :meterId ORDER BY periodEnd DESC")
    fun getByMeterId(meterId: String): Flow<List<BillEntity>>

    @Query("SELECT * FROM bills WHERE meterId = :meterId ORDER BY periodEnd DESC")
    suspend fun getByMeterIdList(meterId: String): List<BillEntity>

    @Query("SELECT * FROM bills WHERE id = :id")
    suspend fun getById(id: String): BillEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bill: BillEntity)
}
