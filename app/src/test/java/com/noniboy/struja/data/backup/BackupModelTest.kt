package com.noniboy.struja.data.backup

import com.google.gson.Gson
import com.noniboy.struja.data.model.Bill
import com.noniboy.struja.data.model.BlockBreakdown
import com.noniboy.struja.data.model.Meter
import com.noniboy.struja.data.model.Reading
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

class BackupModelTest {

    private val gson = Gson()

    private fun sampleEnvelope() = BackupEnvelope(
        version = BACKUP_VERSION,
        exportedAt = "2026-09-11T00:00:00Z",
        meters = listOf(
            MeterBackup(
                meter = Meter(
                    id = "m1", name = "Stan", approvedKw = 3.3,
                    createdAt = "2026-01-01T00:00:00Z"
                ),
                readings = listOf(
                    Reading(
                        id = "r1", meterId = "m1", recordedAt = "2026-08-01",
                        vt = 100, mt = 50, source = "manual",
                        createdAt = "2026-08-01T00:00:00Z"
                    ),
                    Reading(
                        id = "r2", meterId = "m1", recordedAt = "2026-09-01",
                        vt = 150, mt = 70, source = "ai", confidence = "high",
                        createdAt = "2026-09-01T00:00:00Z"
                    )
                ),
                bills = listOf(
                    Bill(
                        id = "b1", meterId = "m1",
                        periodStart = "2026-08-01", periodEnd = "2026-09-01",
                        prevReadingId = "r1", currReadingId = "r2",
                        approvedKw = 3.3, consumptionKwh = 70.0,
                        mjernoMjesto = 2.48, obracunskaSnaga = 11.5,
                        energyCost = 5.0, oieCost = 0.05,
                        subtotal = 20.0, vatAmount = 3.4, total = 23.4,
                        blocks = listOf(
                            BlockBreakdown(
                                label = "Blok I", kwh = 70.0, rate = 0.07,
                                activeEnergyCost = 5.0, transmissionCost = 0.5,
                                distributionCost = 1.0, oieCost = 0.05, totalCost = 6.55
                            )
                        ),
                        createdAt = "2026-09-01T00:00:00Z"
                    )
                )
            )
        )
    )

    @Test
    fun `round-trip preserves all data`() {
        val json = sampleEnvelope().toJson(gson)
        val parsed = parseBackup(json, gson)

        assertEquals(1, parsed.meters.size)
        assertEquals("m1", parsed.meters[0].meter.id)
        assertEquals(2, parsed.meters[0].readings.size)
        assertEquals(1, parsed.meters[0].bills.size)
        assertEquals(150, parsed.meters[0].readings[1].vt)
        assertEquals(70.0, parsed.meters[0].bills[0].consumptionKwh, 0.001)
        assertEquals(1, parsed.meters[0].bills[0].blocks.size)

        val summary = parsed.summary()
        assertEquals(1, summary.meters)
        assertEquals(2, summary.readings)
        assertEquals(1, summary.bills)
    }

    @Test
    fun `corrupt json is rejected`() {
        try {
            parseBackup("{not json", gson)
            fail("expected BackupException")
        } catch (e: BackupException) {
            assertEquals("Datoteka nije ispravna sigurnosna kopija.", e.message)
        }
    }

    @Test
    fun `wrong version is rejected`() {
        val json = sampleEnvelope().copy(version = 99).toJson(gson)
        try {
            parseBackup(json, gson)
            fail("expected BackupException")
        } catch (e: BackupException) {
            assertEquals("Nepodržana verzija kopije (v99).", e.message)
        }
    }

    @Test
    fun `blank meter name is rejected`() {
        val bad = sampleEnvelope().copy(
            meters = listOf(
                sampleEnvelope().meters[0].copy(
                    meter = sampleEnvelope().meters[0].meter.copy(name = "")
                )
            )
        )
        try {
            parseBackup(bad.toJson(gson), gson)
            fail("expected BackupException")
        } catch (e: BackupException) {
            assertEquals("Kopija sadrži brojilo bez naziva.", e.message)
        }
    }

    @Test
    fun `bad reading date is rejected`() {
        val bad = sampleEnvelope().copy(
            meters = listOf(
                sampleEnvelope().meters[0].copy(
                    readings = listOf(
                        sampleEnvelope().meters[0].readings[0].copy(recordedAt = "01.08.2026")
                    )
                )
            )
        )
        try {
            parseBackup(bad.toJson(gson), gson)
            fail("expected BackupException")
        } catch (e: BackupException) {
            assertEquals("Kopija sadrži očitanje s neispravnim datumom.", e.message)
        }
    }
}
