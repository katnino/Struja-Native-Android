package com.noniboy.struja.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meters")
data class MeterEntity(
    @PrimaryKey val id: String,
    val userId: String = "local",
    val name: String,
    val tariffGroup: String = "TG2",
    val approvedKw: Double,
    val notes: String? = null,
    val createdAt: String
)
