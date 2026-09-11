package com.noniboy.struja.domain.tariff

import org.junit.Assert.assertEquals
import org.junit.Test

class TariffCalculatorTest {

    @Test
    fun `splitBlocks returns correct block sizes for small consumption`() {
        val (blockI, blockII, blockIII) = TariffCalculator.splitBlocks(244.0)
        assertEquals(244.0, blockI, 0.001)
        assertEquals(0.0, blockII, 0.001)
        assertEquals(0.0, blockIII, 0.001)
    }

    @Test
    fun `splitBlocks returns correct block sizes for medium consumption`() {
        val (blockI, blockII, blockIII) = TariffCalculator.splitBlocks(800.0)
        assertEquals(500.0, blockI, 0.001)
        assertEquals(300.0, blockII, 0.001)
        assertEquals(0.0, blockIII, 0.001)
    }

    @Test
    fun `splitBlocks returns correct block sizes for large consumption`() {
        val (blockI, blockII, blockIII) = TariffCalculator.splitBlocks(2000.0)
        assertEquals(500.0, blockI, 0.001)
        assertEquals(1000.0, blockII, 0.001)
        assertEquals(500.0, blockIII, 0.001)
    }

    @Test
    fun `calculateBill returns zero for zero consumption`() {
        val result = TariffCalculator.calculateBill(0.0, 0.0)
        assertEquals(0.0, result.total, 0.001)
        assertEquals(0.0, result.totalKwh, 0.001)
    }

    @Test
    fun `calculateBill returns zero for negative values`() {
        val result = TariffCalculator.calculateBill(-10.0, 5.0)
        assertEquals(0.0, result.total, 0.001)
    }

    @Test
    fun `calculateBill produces correct result for known test case`() {
        // From original test: vtKwh=136, mtKwh=108
        val result = TariffCalculator.calculateBill(136.0, 108.0, approvedKw = 3.3)

        assertEquals(244.0, result.totalKwh, 0.001)

        // All consumption is in Block I (< 500 kWh)
        assertEquals(1, result.blocks.size)

        // Total should be around 52.28 KM
        assertEquals(52.28, result.total, 0.1)
    }

    @Test
    fun `calculateBill includes fixed charges for periods gte 29 days`() {
        val result = TariffCalculator.calculateBill(100.0, 100.0, approvedKw = 3.3, daysInPeriod = 30)

        assertEquals(false, result.isPartial)
        assertEquals(2.48, result.mjernoMjesto, 0.001)
        assertEquals(11.51, result.obracunskaSnaga, 0.1)
    }

    @Test
    fun `calculateBill excludes fixed charges for periods lt 29 days`() {
        val result = TariffCalculator.calculateBill(100.0, 100.0, approvedKw = 3.3, daysInPeriod = 15)

        assertEquals(true, result.isPartial)
        assertEquals(0.0, result.mjernoMjesto, 0.001)
        assertEquals(0.0, result.obracunskaSnaga, 0.001)
    }

    @Test
    fun `calculateBill handles VT MT ratio correctly`() {
        // All VT
        val resultVT = TariffCalculator.calculateBill(200.0, 0.0)
        // All MT
        val resultMT = TariffCalculator.calculateBill(0.0, 200.0)

        // VT rates are higher than MT rates, so all-VT bill should be higher
        assert(resultVT.total > resultMT.total)
    }
}
