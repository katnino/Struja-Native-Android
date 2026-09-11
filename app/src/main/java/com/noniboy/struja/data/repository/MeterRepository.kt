package com.noniboy.struja.data.repository

import com.noniboy.struja.data.db.dao.MeterDao
import com.noniboy.struja.data.db.entity.MeterEntity
import com.noniboy.struja.data.model.Meter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MeterRepository @Inject constructor(
    private val meterDao: MeterDao
) {
    fun getAll(): Flow<List<Meter>> {
        return meterDao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun getById(id: String): Meter? {
        return meterDao.getById(id)?.toDomain()
    }

    suspend fun getAllList(): List<Meter> {
        return meterDao.getAllList().map { it.toDomain() }
    }

    suspend fun insert(meter: Meter) {
        meterDao.insert(meter.toEntity())
    }

    suspend fun delete(meter: Meter) {
        meterDao.delete(meter.toEntity())
    }

    suspend fun deleteById(id: String) {
        meterDao.deleteById(id)
    }

    private fun MeterEntity.toDomain() = Meter(
        id = id,
        userId = userId,
        name = name,
        tariffGroup = tariffGroup,
        approvedKw = approvedKw,
        notes = notes,
        createdAt = createdAt
    )

    private fun Meter.toEntity() = MeterEntity(
        id = id,
        userId = userId,
        name = name,
        tariffGroup = tariffGroup,
        approvedKw = approvedKw,
        notes = notes,
        createdAt = createdAt
    )
}
