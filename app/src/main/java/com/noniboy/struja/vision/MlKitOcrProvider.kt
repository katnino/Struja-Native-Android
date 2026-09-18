package com.noniboy.struja.vision

import android.content.Context
import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * On-device OCR via bundled ML Kit Latin model (offline, no API key).
 *
 * Tailored to this meter: digits are white-on-black, everything else is
 * black-on-white. ML Kit expects dark-on-light, so regions are inverted
 * before recognition — digits become readable while surrounding print turns
 * light-on-dark and gets ignored by the model on its own.
 *
 * Pass 0: full frame inverted — rows may sit anywhere, no centering needed.
 * Pass 1: center band inverted, parsed as two rows (tighter, fewer strays).
 * Pass 2: band split into top (VT) / bottom (MT) halves, each inverted
 *         and read independently — rows can't be confused with each other.
 *
 * Strictly local: never touches network, never falls back to Gemini.
 * Callers ([VisionExtractor]) route by the user's Settings-level choice.
 */
@Singleton
class MlKitOcrProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {

    suspend fun extractFromBitmap(bitmap: Bitmap): ExtractResult {
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        try {
            return try {
                // Pass 0: full frame inverted. Rows need not be centered;
                // inversion hides the surrounding print, so no crop needed.
                val fullInverted = MeterImagePreprocessor.invert(bitmap)
                try {
                    OcrDebug.save(context, "full_inv.jpg", fullInverted)
                    val full = MeterOcrParser.parse(
                        recognizeLines(recognizer, fullInverted),
                        imageHeight = null
                    )
                    if (full.confidence == "high" && full.vt != null && full.mt != null) {
                        return full
                    }
                } finally {
                    fullInverted.recycle()
                }

                val band = MeterImagePreprocessor.centerBand(bitmap)
                try {
                    OcrDebug.save(context, "band.jpg", band)
                    // Pass 1: whole band inverted.
                    val invertedBand = MeterImagePreprocessor.invert(band)
                    try {
                        OcrDebug.save(context, "band_inv.jpg", invertedBand)
                        val whole = recognizeLines(recognizer, invertedBand)
                        val parsed = MeterOcrParser.parse(
                            whole,
                            imageHeight = invertedBand.height.toFloat()
                        )
                        if (parsed.confidence == "high" && parsed.vt != null && parsed.mt != null) {
                            return parsed
                        }
                    } finally {
                        invertedBand.recycle()
                    }

                    // Pass 2: rows read independently (top = VT, bottom = MT).
                    val top = MeterImagePreprocessor.topHalf(band)
                    val bottom = MeterImagePreprocessor.bottomHalf(band)
                    try {
                        val vt = bestInRegion(recognizer, top, "row_top_inv.jpg")
                        val mt = bestInRegion(recognizer, bottom, "row_bottom_inv.jpg")
                        if (vt != null && mt != null) {
                            return ExtractResult(vt = vt, mt = mt, confidence = "high")
                        }
                        if (vt != null || mt != null) {
                            return ExtractResult(
                                vt = vt,
                                mt = mt,
                                confidence = "low",
                                note = "Uređaj je pročitao samo jedan red — provjerite vrijednosti prije spremanja."
                            )
                        }
                    } finally {
                        top.recycle()
                        bottom.recycle()
                    }

                    ExtractResult(
                        confidence = "low",
                        note = "Uređaj nije pronašao cifre. Centrirajte cifre na sredinu kadra i približite."
                    )
                } finally {
                    band.recycle()
                }
            } catch (e: Exception) {
                ExtractResult(
                    confidence = "low",
                    note = "Uređaj nije uspio pročitati sliku: ${e.message}"
                )
            }
        } finally {
            recognizer.close()
        }
    }

    private suspend fun bestInRegion(recognizer: TextRecognizer, region: Bitmap, debugName: String): Int? {
        val inverted = MeterImagePreprocessor.invert(region)
        try {
            OcrDebug.save(context, debugName, inverted)
            return MeterOcrParser.bestSingle(recognizeLines(recognizer, inverted))
        } finally {
            inverted.recycle()
        }
    }

    private suspend fun recognizeLines(recognizer: TextRecognizer, bmp: Bitmap): List<OcrLine> {
        val result = recognizer.process(InputImage.fromBitmap(bmp, 0)).await()
        return result.textBlocks.flatMap { block ->
            block.lines.mapNotNull { line ->
                val box = line.boundingBox ?: return@mapNotNull null
                if (!line.text.any { it.isDigit() }) return@mapNotNull null
                OcrLine(
                    text = line.text,
                    centerY = box.exactCenterY(),
                    boxHeight = box.height().toFloat().coerceAtLeast(1f)
                )
            }
        }
    }
}
