package com.noniboy.struja.domain.outlook

import com.noniboy.struja.data.model.Reading
import com.noniboy.struja.domain.tariff.BillResult
import com.noniboy.struja.domain.tariff.TariffCalculator
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoUnit

data class ActualMonthProgress(
    val vtKwh: Double,
    val mtKwh: Double,
    val daysElapsed: Long,
    val lastReadingDate: String
)

data class RunRate(
    val vtPerDay: Double,
    val mtPerDay: Double,
    val source: String,
    val basedOnDays: Long
)

enum class Confidence {
    MEASURED,
    PROJECTED,
    INSUFFICIENT_DATA
}

data class MonthOutlook(
    val year: Int,
    val month: Int,
    val daysInMonth: Int,
    val actual: ActualMonthProgress?,
    val runRate: RunRate?,
    val totalEstimatedVt: Double,
    val totalEstimatedMt: Double,
    val bill: BillResult?,
    val confidence: Confidence
)

object MonthOutlookCalculator {

    private const val ROLLING_WINDOW_DAYS = 3L

    fun calculate(
        readings: List<Reading>,
        year: Int,
        month: Int,
        approvedKw: Double
    ): MonthOutlook {
        val monthKey = YearMonth.of(year, month)
        val daysInMonth = monthKey.lengthOfMonth()
        val monthStart = monthKey.atDay(1)
        val monthEnd = monthKey.atEndOfMonth()

        val actual = getActualMonthProgress(readings, year, month)
        val runRate = deriveRunRate(readings)

        if (actual == null && runRate == null) {
            return MonthOutlook(
                year = year,
                month = month,
                daysInMonth = daysInMonth,
                actual = null,
                runRate = null,
                totalEstimatedVt = 0.0,
                totalEstimatedMt = 0.0,
                bill = null,
                confidence = Confidence.INSUFFICIENT_DATA
            )
        }

        val daysElapsed = actual?.daysElapsed ?: 0
        val daysRemaining = maxOf(daysInMonth - daysElapsed, 0)

        val projectedRemainingVt = runRate?.let { it.vtPerDay * daysRemaining } ?: 0.0
        val projectedRemainingMt = runRate?.let { it.mtPerDay * daysRemaining } ?: 0.0

        val totalEstimatedVt = (actual?.vtKwh ?: 0.0) + projectedRemainingVt
        val totalEstimatedMt = (actual?.mtKwh ?: 0.0) + projectedRemainingMt

        val bill = if (totalEstimatedVt > 0 || totalEstimatedMt > 0) {
            TariffCalculator.calculateBill(totalEstimatedVt, totalEstimatedMt, approvedKw)
        } else null

        val today = LocalDate.now()
        val currentMonth = today.year == year && today.monthValue == month
        val confidence = when {
            actual != null && daysRemaining <= 0 -> Confidence.MEASURED
            actual != null || runRate != null -> Confidence.PROJECTED
            else -> Confidence.INSUFFICIENT_DATA
        }

        return MonthOutlook(
            year = year,
            month = month,
            daysInMonth = daysInMonth,
            actual = actual,
            runRate = runRate,
            totalEstimatedVt = totalEstimatedVt,
            totalEstimatedMt = totalEstimatedMt,
            bill = bill,
            confidence = confidence
        )
    }

    private fun getActualMonthProgress(
        readings: List<Reading>,
        year: Int,
        month: Int
    ): ActualMonthProgress? {
        if (readings.isEmpty()) return null

        val sorted = readings.sortedBy { it.recordedAt }
        val monthKey = YearMonth.of(year, month)
        val monthStart = monthKey.atDay(1)
        val monthEnd = monthKey.atEndOfMonth()

        // Find readings strictly before the month (baseline candidates)
        val readingsBeforeMonth = sorted.filter {
            LocalDate.parse(it.recordedAt) < monthStart
        }

        // Find readings within the month
        val readingsInMonth = sorted.filter {
            val date = LocalDate.parse(it.recordedAt)
            !date.isBefore(monthStart) && !date.isAfter(monthEnd)
        }

        if (readingsInMonth.isEmpty()) return null

        // Need a baseline (reading before the month) OR at least 2 readings in the month
        val baseline = readingsBeforeMonth.lastOrNull()
        val firstInMonth = readingsInMonth.first()
        val lastInMonth = readingsInMonth.last()

        // Use baseline if available, otherwise use first in-month reading
        val effectiveBaseline = baseline ?: firstInMonth
        val effectiveLast = lastInMonth

        // If baseline and last are the same reading, we can't compute a delta
        if (effectiveBaseline.id == effectiveLast.id) return null

        val baselineDate = LocalDate.parse(effectiveBaseline.recordedAt)
        val lastDate = LocalDate.parse(effectiveLast.recordedAt)

        val vtKwh = maxOf((effectiveLast.vt ?: 0) - (effectiveBaseline.vt ?: 0), 0).toDouble()
        val mtKwh = maxOf((effectiveLast.mt ?: 0) - (effectiveBaseline.mt ?: 0), 0).toDouble()
        val daysElapsed = maxOf(ChronoUnit.DAYS.between(baselineDate, lastDate), 1)

        return ActualMonthProgress(
            vtKwh = vtKwh,
            mtKwh = mtKwh,
            daysElapsed = daysElapsed,
            lastReadingDate = effectiveLast.recordedAt
        )
    }

    private fun deriveRunRate(readings: List<Reading>): RunRate? {
        if (readings.size < 2) return null

        val sorted = readings.sortedBy { it.recordedAt }
        val latest = sorted.last()
        val latestDate = LocalDate.parse(latest.recordedAt)

        var ref: Reading? = null
        for (i in sorted.size - 2 downTo 0) {
            val candidate = sorted[i]
            val candidateDate = LocalDate.parse(candidate.recordedAt)
            val daysBetween = ChronoUnit.DAYS.between(candidateDate, latestDate)
            if (daysBetween in 1..ROLLING_WINDOW_DAYS) {
                ref = candidate
            } else {
                break
            }
        }

        if (ref == null) {
            ref = sorted[sorted.size - 2]
        }

        val refDate = LocalDate.parse(ref.recordedAt)
        val days = ChronoUnit.DAYS.between(refDate, latestDate)
        if (days <= 0) return null

        val vtPerDay = maxOf((latest.vt ?: 0) - (ref.vt ?: 0), 0).toDouble() / days
        val mtPerDay = maxOf((latest.mt ?: 0) - (ref.mt ?: 0), 0).toDouble() / days

        val source = if (days >= ROLLING_WINDOW_DAYS) "rolling_average" else "last_interval"

        return RunRate(
            vtPerDay = vtPerDay,
            mtPerDay = mtPerDay,
            source = source,
            basedOnDays = days
        )
    }
}
