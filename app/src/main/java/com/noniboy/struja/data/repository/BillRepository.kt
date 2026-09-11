package com.noniboy.struja.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.noniboy.struja.data.db.dao.BillDao
import com.noniboy.struja.data.db.entity.BillEntity
import com.noniboy.struja.data.model.Bill
import com.noniboy.struja.data.model.BlockBreakdown
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BillRepository @Inject constructor(
    private val billDao: BillDao,
    private val gson: Gson
) {
    fun getByMeterId(meterId: String): Flow<List<Bill>> {
        return billDao.getByMeterId(meterId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun getById(id: String): Bill? {
        return billDao.getById(id)?.toDomain()
    }

    suspend fun getByMeterIdList(meterId: String): List<Bill> {
        return billDao.getByMeterIdList(meterId).map { it.toDomain() }
    }

    suspend fun insert(bill: Bill) {
        billDao.insert(bill.toEntity())
    }

    private fun BillEntity.toDomain() = Bill(
        id = id,
        meterId = meterId,
        userId = userId,
        periodStart = periodStart,
        periodEnd = periodEnd,
        prevReadingId = prevReadingId,
        currReadingId = currReadingId,
        approvedKw = approvedKw,
        consumptionKwh = consumptionKwh,
        mjernoMjesto = mjernoMjesto,
        obracunskaSnaga = obracunskaSnaga,
        energyCost = energyCost,
        transmissionBaseCost = transmissionBaseCost,
        totalTransmission = totalTransmission,
        distributionBaseCost = distributionBaseCost,
        totalDistribution = totalDistribution,
        oieCost = oieCost,
        subtotal = subtotal,
        vatAmount = vatAmount,
        total = total,
        blocks = parseBlocks(blocksJson),
        isPartial = isPartial,
        createdAt = createdAt
    )

    private fun Bill.toEntity() = BillEntity(
        id = id,
        meterId = meterId,
        userId = userId,
        periodStart = periodStart,
        periodEnd = periodEnd,
        prevReadingId = prevReadingId,
        currReadingId = currReadingId,
        approvedKw = approvedKw,
        consumptionKwh = consumptionKwh,
        mjernoMjesto = mjernoMjesto,
        obracunskaSnaga = obracunskaSnaga,
        energyCost = energyCost,
        transmissionBaseCost = transmissionBaseCost,
        totalTransmission = totalTransmission,
        distributionBaseCost = distributionBaseCost,
        totalDistribution = totalDistribution,
        oieCost = oieCost,
        subtotal = subtotal,
        vatAmount = vatAmount,
        total = total,
        blocksJson = serializeBlocks(blocks),
        isPartial = isPartial,
        createdAt = createdAt
    )

    private fun parseBlocks(json: String): List<BlockBreakdown> {
        if (json.isBlank()) return emptyList()
        val type = object : TypeToken<List<BlockBreakdown>>() {}.type
        return gson.fromJson(json, type)
    }

    private fun serializeBlocks(blocks: List<BlockBreakdown>): String {
        return gson.toJson(blocks)
    }
}
