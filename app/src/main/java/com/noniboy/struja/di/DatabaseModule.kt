package com.noniboy.struja.di

import android.content.Context
import androidx.room.Room
import com.noniboy.struja.data.db.StrujaDatabase
import com.noniboy.struja.data.db.dao.BillDao
import com.noniboy.struja.data.db.dao.MeterDao
import com.noniboy.struja.data.db.dao.ReadingDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): StrujaDatabase {
        return Room.databaseBuilder(
            context,
            StrujaDatabase::class.java,
            "struja.db"
        ).fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    fun provideMeterDao(database: StrujaDatabase): MeterDao {
        return database.meterDao()
    }

    @Provides
    fun provideReadingDao(database: StrujaDatabase): ReadingDao {
        return database.readingDao()
    }

    @Provides
    fun provideBillDao(database: StrujaDatabase): BillDao {
        return database.billDao()
    }
}
