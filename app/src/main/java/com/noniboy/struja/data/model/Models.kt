package com.noniboy.struja.data.model

data class Meter(
    val id: String,
    val userId: String = "local",
    val name: String,
    val tariffGroup: String = "TG2",
    val approvedKw: Double,
    val notes: String? = null,
    val createdAt: String
)

data class Reading(
    val id: String,
    val meterId: String,
    val userId: String = "local",
    val recordedAt: String,
    val vt: Int?,
    val mt: Int?,
    val source: String,
    val confidence: String? = null,
    val createdAt: String
)

data class Bill(
    val id: String,
    val meterId: String,
    val userId: String = "local",
    val periodStart: String,
    val periodEnd: String,
    val prevReadingId: String?,
    val currReadingId: String?,
    val approvedKw: Double,
    val consumptionKwh: Double,
    val mjernoMjesto: Double,
    val obracunskaSnaga: Double,
    val energyCost: Double,
    val transmissionBaseCost: Double = 0.0,
    val totalTransmission: Double = 0.0,
    val distributionBaseCost: Double = 0.0,
    val totalDistribution: Double = 0.0,
    val oieCost: Double,
    val subtotal: Double,
    val vatAmount: Double,
    val total: Double,
    val blocks: List<BlockBreakdown>,
    val isPartial: Boolean = false,
    val createdAt: String
)

data class BlockBreakdown(
    val label: String,
    val kwh: Double,
    val rate: Double,
    val activeEnergyCost: Double,
    val transmissionCost: Double,
    val distributionCost: Double,
    val oieCost: Double,
    val totalCost: Double
)
