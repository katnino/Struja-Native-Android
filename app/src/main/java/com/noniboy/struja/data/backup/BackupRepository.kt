package com.noniboy.struja.data.backup

import androidx.room.withTransaction
import com.google.gson.Gson
import com.noniboy.struja.data.db.StrujaDatabase
import com.noniboy.struja.data.repository.BillRepository
import com.noniboy.struja.data.repository.MeterRepository
import com.noniboy.struja.data.repository.ReadingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Full export / import of meters + readings + bills as one versioned JSON file.
 * No API keys or settings are included — the backup is pure meter data.
 * Import merges with REPLACE (re-importing the same file is idempotent).
 */
@Singleton
class BackupRepository @Inject constructor(
    private val database: StrujaDatabase,
    private val meterRepository: MeterRepository,
    private val readingRepository: ReadingRepository,
    private val billRepository: BillRepository,
    private val gson: Gson
) {

    suspend fun exportJson(): String = withContext(Dispatchers.IO) {
        val bundles = meterRepository.getAllList().map { meter ->
            MeterBackup(
                meter = meter,
                readings = readingRepository.getByMeterIdList(meter.id),
                bills = billRepository.getByMeterIdList(meter.id)
            )
        }
        BackupEnvelope(
            version = BACKUP_VERSION,
            exportedAt = Instant.now().toString(),
            meters = bundles
        ).toJson(gson)
    }

    suspend fun importJson(json: String): ImportSummary = withContext(Dispatchers.IO) {
        importEnvelope(parseBackup(json, gson))
    }

    suspend fun importEnvelope(envelope: BackupEnvelope): ImportSummary = withContext(Dispatchers.IO) {
        validateBackup(envelope)
        database.withTransaction {
            envelope.meters.forEach { bundle ->
                val meterId = bundle.meter.id
                meterRepository.insert(bundle.meter)
                bundle.readings.forEach { reading ->
                    readingRepository.insert(reading.copy(meterId = meterId))
                }
                bundle.bills.forEach { bill ->
                    billRepository.insert(bill.copy(meterId = meterId))
                }
            }
        }
        envelope.summary()
    }
}
