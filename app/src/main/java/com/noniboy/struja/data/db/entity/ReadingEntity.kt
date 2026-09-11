package com.noniboy.struja.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "readings",
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
data class ReadingEntity(
    @PrimaryKey val id: String,
    val meterId: String,
    val userId: String = "local",
    val recordedAt: String,
    val vt: Int?,
    val mt: Int?,
    val source: String,
    val confidence: String? = null,
    val createdAt: String
)
