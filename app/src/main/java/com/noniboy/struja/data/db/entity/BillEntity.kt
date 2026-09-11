package com.noniboy.struja.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "bills",
    foreignKeys = [
        ForeignKey(
            entity = MeterEntity::class,
            parentColumns = ["id"],
            childColumns = ["meterId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["meterId"])]
)
data class BillEntity(
    @PrimaryKey val id: String,
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
    val blocksJson: String,
    val isPartial: Boolean = false,
    val createdAt: String
)
