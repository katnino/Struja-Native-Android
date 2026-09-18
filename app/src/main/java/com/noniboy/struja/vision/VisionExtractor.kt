package com.noniboy.struja.vision

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.util.Base64
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VisionExtractor @Inject constructor(
    @ApplicationContext private val context: Context,
    private val geminiProvider: GeminiProvider,
    private val mlKitOcrProvider: MlKitOcrProvider
) {
    private val prefs = context.getSharedPreferences("struja_settings", Context.MODE_PRIVATE)

    fun getApiKey(): String {
        return prefs.getString("gemini_api_key", "") ?: ""
    }

    fun getOcrEngine(): OcrEngine {
        return OcrEngine.read(context)
    }

    /**
     * Safest-route date source: EXIF capture time of the photo.
     * Returns null when missing, unparseable, or in the future —
     * callers must keep the current manual date in that case.
     */
    fun extractPhotoDate(uri: Uri): LocalDate? {
        return try {
            val raw = context.contentResolver.openInputStream(uri)?.use { stream ->
                val exif = ExifInterface(stream)
                exif.getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL)
                    ?: exif.getAttribute(ExifInterface.TAG_DATETIME)
            } ?: return null
            val parsed = try {
                java.time.LocalDateTime.parse(raw.trim(), DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss")).toLocalDate()
            } catch (e: Exception) {
                return null
            }
            if (parsed.isAfter(LocalDate.now())) null else parsed
        } catch (e: Exception) {
            null
        }
    }

    suspend fun extractFromUri(uri: Uri): ExtractResult {
        return extractFromUri(uri, getOcrEngine())
    }

    /**
     * Strict routing — no auto-fallback. LOCAL never touches network/API key,
     * GEMINI behaves exactly as before.
     */
    suspend fun extractFromUri(uri: Uri, engine: OcrEngine): ExtractResult {
        return when (engine) {
            OcrEngine.LOCAL -> extractLocal(uri)
            OcrEngine.GEMINI -> extractViaGemini(uri)
        }
    }

    private suspend fun extractLocal(uri: Uri): ExtractResult {
        val bitmap = loadBitmap(uri, maxDimension = 1568)
            ?: return ExtractResult(confidence = "low", note = "Nije moguće učitati sliku")
        try {
            return mlKitOcrProvider.extractFromBitmap(bitmap)
        } finally {
            // loadBitmap returns a fresh owned bitmap (originals already recycled inside).
            bitmap.recycle()
        }
    }

    private suspend fun extractViaGemini(uri: Uri): ExtractResult {
        val apiKey = getApiKey()
        if (apiKey.isBlank()) {
            return ExtractResult(confidence = "low", note = "API key nije podešen. Podesite ga u podešavanjima.")
        }

        val bytes = loadScaledJpeg(uri, maxDimension = 1568, quality = 85)
            ?: return ExtractResult(confidence = "low", note = "Nije moguće učitati sliku")

        val base64 = Base64.encodeToString(bytes, Base64.NO_WRAP)
        val mediaType = "image/jpeg"

        return geminiProvider.extract(apiKey, base64, mediaType)
    }

    private fun loadScaledJpeg(uri: Uri, maxDimension: Int, quality: Int): ByteArray? {
        val bitmap = loadBitmap(uri, maxDimension) ?: return null
        return try {
            val out = java.io.ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)
            out.toByteArray()
        } catch (e: Exception) {
            null
        } finally {
            bitmap.recycle()
        }
    }

    /** Decode + EXIF-rotate + scale down. Caller owns the returned bitmap. */
    private fun loadBitmap(uri: Uri, maxDimension: Int): Bitmap? {
        return try {
            val bitmap = context.contentResolver.openInputStream(uri)?.use { stream ->
                BitmapFactory.decodeStream(stream)
            } ?: return null
            val rotated = applyExifRotation(uri, bitmap)
            val scaled = scaleDown(rotated, maxDimension)
            if (scaled !== rotated) rotated.recycle()
            if (rotated !== bitmap) bitmap.recycle()
            scaled
        } catch (e: Exception) {
            null
        }
    }

    private fun applyExifRotation(uri: Uri, bitmap: Bitmap): Bitmap {
        return try {
            val orientation = context.contentResolver.openInputStream(uri)?.use { stream ->
                ExifInterface(stream).getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                )
            } ?: ExifInterface.ORIENTATION_NORMAL
            val degrees = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                else -> 0f
            }
            if (degrees == 0f) return bitmap
            val matrix = Matrix().apply { postRotate(degrees) }
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        } catch (e: Exception) {
            bitmap
        }
    }

    private fun scaleDown(bitmap: Bitmap, maxDimension: Int): Bitmap {
        val maxSide = maxOf(bitmap.width, bitmap.height)
        if (maxSide <= maxDimension) return bitmap
        val scale = maxDimension.toFloat() / maxSide
        val w = (bitmap.width * scale).toInt().coerceAtLeast(1)
        val h = (bitmap.height * scale).toInt().coerceAtLeast(1)
        return Bitmap.createScaledBitmap(bitmap, w, h, true)
    }
}
