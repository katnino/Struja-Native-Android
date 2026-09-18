package com.noniboy.struja.vision

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import java.io.File

/**
 * Dumps on-device OCR intermediates (center band, inverted band, row halves)
 * to cacheDir/ocr-debug/ — overwritten every run, last run only.
 * Pull via Android Studio Device Explorer
 * (/data/data/com.noniboy.struja/cache/ocr-debug/) to see what the
 * local engine actually looked at. Never breaks extraction: all failures
 * are swallowed after a Logcat line.
 */
object OcrDebug {
    private const val TAG = "OcrDebug"

    fun save(context: Context, name: String, bitmap: Bitmap) {
        try {
            val dir = File(context.cacheDir, "ocr-debug").apply { mkdirs() }
            File(dir, name).outputStream().use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            }
            Log.d(TAG, "saved $name (${bitmap.width}x${bitmap.height})")
        } catch (e: Exception) {
            Log.d(TAG, "dump failed for $name: ${e.message}")
        }
    }
}
