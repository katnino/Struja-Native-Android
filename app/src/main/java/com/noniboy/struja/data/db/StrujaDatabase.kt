package com.noniboy.struja.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.noniboy.struja.data.db.dao.BillDao
import com.noniboy.struja.data.db.dao.MeterDao
import com.noniboy.struja.data.db.dao.ReadingDao
import com.noniboy.struja.data.db.entity.BillEntity
import com.noniboy.struja.data.db.entity.MeterEntity
import com.noniboy.struja.data.db.entity.ReadingEntity

@Database(
    entities = [MeterEntity::class, ReadingEntity::class, BillEntity::class],
    version = 1,
    exportSchema = false
)
abstract class StrujaDatabase : RoomDatabase() {
    abstract fun meterDao(): MeterDao
    abstract fun readingDao(): ReadingDao
    abstract fun billDao(): BillDao
}
