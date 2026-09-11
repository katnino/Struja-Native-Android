package com.noniboy.struja.data.repository

import com.noniboy.struja.data.db.dao.ReadingDao
import com.noniboy.struja.data.db.entity.ReadingEntity
import com.noniboy.struja.data.model.Reading
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReadingRepository @Inject constructor(
    private val readingDao: ReadingDao
) {
    fun getByMeterId(meterId: String): Flow<List<Reading>> {
        return readingDao.getByMeterId(meterId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun getLatestByMeterId(meterId: String): Reading? {
        return readingDao.getLatestByMeterId(meterId)?.toDomain()
    }

    suspend fun getByMeterIdList(meterId: String): List<Reading> {
        return readingDao.getByMeterIdList(meterId).map { it.toDomain() }
    }

    suspend fun insert(reading: Reading) {
        readingDao.insert(reading.toEntity())
    }

    private fun ReadingEntity.toDomain() = Reading(
        id = id,
        meterId = meterId,
        userId = userId,
        recordedAt = recordedAt,
        vt = vt,
        mt = mt,
        source = source,
        confidence = confidence,
        createdAt = createdAt
    )

    private fun Reading.toEntity() = ReadingEntity(
        id = id,
        meterId = meterId,
        userId = userId,
        recordedAt = recordedAt,
        vt = vt,
        mt = mt,
        source = source,
        confidence = confidence,
        createdAt = createdAt
    )
}
