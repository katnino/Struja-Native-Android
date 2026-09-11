package com.noniboy.struja.data.backup

import com.google.gson.Gson
import com.noniboy.struja.data.model.Bill
import com.noniboy.struja.data.model.Meter
import com.noniboy.struja.data.model.Reading
import java.time.LocalDate

const val BACKUP_VERSION = 1

class BackupException(message: String) : Exception(message)

data class MeterBackup(
    val meter: Meter,
    val readings: List<Reading> = emptyList(),
    val bills: List<Bill> = emptyList()
)

data class BackupEnvelope(
    val version: Int = BACKUP_VERSION,
    val exportedAt: String = "",
    val meters: List<MeterBackup> = emptyList()
)

data class ImportSummary(
    val meters: Int,
    val readings: Int,
    val bills: Int
)

fun BackupEnvelope.summary() = ImportSummary(
    meters = meters.size,
    readings = meters.sumOf { it.readings.size },
    bills = meters.sumOf { it.bills.size }
)

fun BackupEnvelope.toJson(gson: Gson): String = gson.toJson(this)

/**
 * Pure parse + validate step (no DB writes). Throws [BackupException]
 * with a user-facing message when the file is not a valid backup.
 */
fun parseBackup(json: String, gson: Gson): BackupEnvelope {
    val envelope = try {
        gson.fromJson(json, BackupEnvelope::class.java)
            ?: throw BackupException("Datoteka nije ispravna sigurnosna kopija.")
    } catch (e: BackupException) {
        throw e
    } catch (e: Exception) {
        throw BackupException("Datoteka nije ispravna sigurnosna kopija.")
    }
    validateBackup(envelope)
    return envelope
}

fun validateBackup(envelope: BackupEnvelope) {
    if (envelope.version != BACKUP_VERSION) {
        throw BackupException("Nepodržana verzija kopije (v${envelope.version}).")
    }
    envelope.meters.forEach { bundle ->
        if (bundle.meter.id.isBlank() || bundle.meter.name.isBlank()) {
            throw BackupException("Kopija sadrži brojilo bez naziva.")
        }
        bundle.readings.forEach { reading ->
            if (reading.id.isBlank()) {
                throw BackupException("Kopija sadrži očitanje bez identifikatora.")
            }
            try {
                LocalDate.parse(reading.recordedAt)
            } catch (e: Exception) {
                throw BackupException("Kopija sadrži očitanje s neispravnim datumom.")
            }
            if ((reading.vt ?: 0) < 0 || (reading.mt ?: 0) < 0) {
                throw BackupException("Kopija sadrži neispravne vrijednosti brojila.")
            }
        }
        bundle.bills.forEach { bill ->
            if (bill.id.isBlank()) {
                throw BackupException("Kopija sadrži račun bez identifikatora.")
            }
        }
    }
}
