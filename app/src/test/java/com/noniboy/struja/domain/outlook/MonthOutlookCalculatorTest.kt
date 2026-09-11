package com.noniboy.struja.domain.outlook

import com.noniboy.struja.data.model.Reading
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class MonthOutlookCalculatorTest {

    private fun createReading(
        vt: Int,
        mt: Int,
        recordedAt: String
    ) = Reading(
        id = "test-$recordedAt",
        meterId = "meter-1",
        recordedAt = recordedAt,
        vt = vt,
        mt = mt,
        source = "manual",
        createdAt = recordedAt
    )

    @Test
    fun `calculate returns insufficient_data for empty readings`() {
        val outlook = MonthOutlookCalculator.calculate(
            readings = emptyList(),
            year = 2024,
            month = 6,
            approvedKw = 3.3
        )
        assertEquals(Confidence.INSUFFICIENT_DATA, outlook.confidence)
        assertNull(outlook.bill)
    }

    @Test
    fun `calculate returns insufficient_data for single reading`() {
        val readings = listOf(createReading(100, 80, "2024-06-15"))
        val outlook = MonthOutlookCalculator.calculate(
            readings = readings,
            year = 2024,
            month = 6,
            approvedKw = 3.3
        )
        // Single reading with no baseline -> actual is null, runRate needs 2 readings
        assertEquals(Confidence.INSUFFICIENT_DATA, outlook.confidence)
    }

    @Test
    fun `calculate returns projected for readings with run rate`() {
        val readings = listOf(
            createReading(100, 80, "2024-06-01"),
            createReading(150, 120, "2024-06-10")
        )
        val outlook = MonthOutlookCalculator.calculate(
            readings = readings,
            year = 2024,
            month = 6,
            approvedKw = 3.3
        )
        assertEquals(Confidence.PROJECTED, outlook.confidence)
        assertNotNull(outlook.bill)
        assertNotNull(outlook.runRate)
    }

    @Test
    fun `calculate returns measured for readings spanning full month`() {
        // Need a baseline before the month and a reading at the end
        val readings = listOf(
            createReading(100, 80, "2024-05-28"),
            createReading(200, 160, "2024-06-30")
        )
        val outlook = MonthOutlookCalculator.calculate(
            readings = readings,
            year = 2024,
            month = 6,
            approvedKw = 3.3
        )
        // With baseline before month and last reading at end of month (day 30), daysRemaining = 0
        assertEquals(Confidence.MEASURED, outlook.confidence)
    }

    @Test
    fun `calculate projects remaining days correctly`() {
        val readings = listOf(
            createReading(100, 80, "2024-06-01"),
            createReading(150, 120, "2024-06-10")
        )
        val outlook = MonthOutlookCalculator.calculate(
            readings = readings,
            year = 2024,
            month = 6,
            approvedKw = 3.3
        )

        assertNotNull(outlook.actual)
        assertEquals(30, outlook.daysInMonth)
        // Both readings are in-month, baseline = first (June 1), last = June 10
        // daysElapsed = 9
        assertEquals(9, outlook.actual!!.daysElapsed)
    }
}
