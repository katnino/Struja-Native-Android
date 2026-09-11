package com.noniboy.struja.di

import com.google.gson.Gson
import com.noniboy.struja.data.db.dao.BillDao
import com.noniboy.struja.data.db.dao.MeterDao
import com.noniboy.struja.data.db.dao.ReadingDao
import com.noniboy.struja.data.repository.BillRepository
import com.noniboy.struja.data.repository.MeterRepository
import com.noniboy.struja.data.repository.ReadingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMeterRepository(meterDao: MeterDao): MeterRepository {
        return MeterRepository(meterDao)
    }

    @Provides
    @Singleton
    fun provideReadingRepository(readingDao: ReadingDao): ReadingRepository {
        return ReadingRepository(readingDao)
    }

    @Provides
    @Singleton
    fun provideBillRepository(billDao: BillDao, gson: Gson): BillRepository {
        return BillRepository(billDao, gson)
    }
}
