package com.noniboy.struja.vision

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MeterOcrParserTest {

    @Test
    fun `clean two-row meter returns high confidence`() {
        val lines = listOf(
            OcrLine("82345", centerY = 100f, boxHeight = 40f),
            OcrLine("52341", centerY = 200f, boxHeight = 40f)
        )
        val result = MeterOcrParser.parse(lines, imageHeight = 400f)
        assertEquals(82345, result.vt)
        assertEquals(52341, result.mt)
        assertEquals("high", result.confidence)
    }

    @Test
    fun `red decimal sixth digit is stripped`() {
        val lines = listOf(
            OcrLine("823456", centerY = 100f, boxHeight = 40f),
            OcrLine("523418", centerY = 200f, boxHeight = 40f)
        )
        val result = MeterOcrParser.parse(lines, imageHeight = 400f)
        assertEquals(82345, result.vt)
        assertEquals(52341, result.mt)
    }

    @Test
    fun `serials and small specs are ignored`() {
        val lines = listOf(
            OcrLine("82345", centerY = 100f, boxHeight = 40f),
            OcrLine("kWh", centerY = 150f, boxHeight = 30f),
            OcrLine("52341", centerY = 200f, boxHeight = 40f),
            // Serial below disc: long + small + low on image.
            OcrLine("No 12345678", centerY = 350f, boxHeight = 12f),
            OcrLine("3x230V 50Hz", centerY = 370f, boxHeight = 10f)
        )
        val result = MeterOcrParser.parse(lines, imageHeight = 400f)
        assertEquals(82345, result.vt)
        assertEquals(52341, result.mt)
        assertEquals("high", result.confidence)
    }

    @Test
    fun `single readable row returns partial low confidence without overwrite blunt`() {
        val lines = listOf(
            OcrLine("82345", centerY = 100f, boxHeight = 40f)
        )
        val result = MeterOcrParser.parse(lines, imageHeight = 400f)
        assertEquals(82345, result.vt)
        assertNull(result.mt)
        assertEquals("low", result.confidence)
    }

    @Test
    fun `empty input returns low confidence`() {
        val result = MeterOcrParser.parse(emptyList(), imageHeight = 400f)
        assertEquals("low", result.confidence)
        assertNull(result.vt)
        assertNull(result.mt)
    }

    @Test
    fun `only serials returns low confidence`() {
        val lines = listOf(
            OcrLine("No 12345678", centerY = 350f, boxHeight = 12f)
        )
        val result = MeterOcrParser.parse(lines, imageHeight = 400f)
        assertEquals("low", result.confidence)
    }

    @Test
    fun `bestSingle returns best value from one row`() {
        val lines = listOf(
            OcrLine("82345", centerY = 50f, boxHeight = 40f),
            OcrLine("No 12", centerY = 60f, boxHeight = 10f)
        )
        assertEquals(82345, MeterOcrParser.bestSingle(lines))
    }

    @Test
    fun `bestSingle strips red decimal`() {
        val lines = listOf(OcrLine("523418", centerY = 50f, boxHeight = 40f))
        assertEquals(52341, MeterOcrParser.bestSingle(lines))
    }

    @Test
    fun `bestSingle returns null when only serials`() {
        val lines = listOf(OcrLine("No 12345678", centerY = 50f, boxHeight = 12f))
        assertNull(MeterOcrParser.bestSingle(lines))
    }

    @Test
    fun `fifth digit split from red decimal is rejoined`() {
        // ML Kit often reads "8234 56" (5th main glued to red decimal).
        val lines = listOf(
            OcrLine("8234 56", centerY = 100f, boxHeight = 40f),
            OcrLine("52341", centerY = 200f, boxHeight = 40f)
        )
        val result = MeterOcrParser.parse(lines, imageHeight = 400f)
        assertEquals(82345, result.vt)
        assertEquals(52341, result.mt)
        assertEquals("high", result.confidence)
    }

    @Test
    fun `bestSingle rejoins split groups`() {
        assertEquals(12345, MeterOcrParser.bestSingle(
            listOf(OcrLine("1234 56", centerY = 50f, boxHeight = 40f))
        ))
    }

    @Test
    fun `joined serial-length runs are still rejected`() {
        // "12 82345" must not become a 7-char glue; the real reading still wins.
        val lines = listOf(OcrLine("12 82345", centerY = 50f, boxHeight = 40f))
        assertEquals(82345, MeterOcrParser.bestSingle(lines))
    }

    @Test
    fun `full-frame off-center rows parse without area cut`() {
        // Rows sitting low in frame (the failing geometry): no area filter applied.
        val lines = listOf(
            OcrLine("82345", centerY = 900f, boxHeight = 60f),
            OcrLine("52341", centerY = 1100f, boxHeight = 60f)
        )
        val result = MeterOcrParser.parse(lines, imageHeight = null)
        assertEquals(82345, result.vt)
        assertEquals(52341, result.mt)
        assertEquals("high", result.confidence)
    }
}
