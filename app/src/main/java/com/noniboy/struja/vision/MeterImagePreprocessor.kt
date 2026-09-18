package com.noniboy.struja.vision

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint

/**
 * Bitmap prep tailored to this meter's physical layout:
 *  - the two digit rows sit ~center frame,
 *  - digits are white-on-black (everything else is black-on-white).
 *
 * ML Kit's Latin model expects dark-on-light text, so we crop the center
 * band (dropping edge serials/specs physically) and invert it (digits become
 * dark-on-light). Every function returns an *owned* copy — callers recycle.
 */
object MeterImagePreprocessor {

    /** Middle horizontal band where the digit wheels live. */
    fun centerBand(
        src: Bitmap,
        heightFraction: Float = 0.62f,
        widthMarginFraction: Float = 0.04f
    ): Bitmap {
        val w = (src.width * (1f - 2f * widthMarginFraction)).toInt().coerceIn(1, src.width)
        val h = (src.height * heightFraction).toInt().coerceIn(1, src.height)
        val x = ((src.width - w) / 2).coerceIn(0, src.width - w)
        val y = ((src.height - h) / 2).coerceIn(0, src.height - h)
        return ownedCrop(src, x, y, w, h)
    }

    /** Photographic negative: white-on-black digits become black-on-white. */
    fun invert(src: Bitmap): Bitmap {
        val out = Bitmap.createBitmap(src.width, src.height, Bitmap.Config.ARGB_8888)
        val paint = Paint().apply {
            colorFilter = ColorMatrixColorFilter(
                ColorMatrix(
                    floatArrayOf(
                        -1f, 0f, 0f, 0f, 255f,
                        0f, -1f, 0f, 0f, 255f,
                        0f, 0f, -1f, 0f, 255f,
                        0f, 0f, 0f, 1f, 0f
                    )
                )
            )
        }
        Canvas(out).drawBitmap(src, 0f, 0f, paint)
        return out
    }

    /** Top row region (VT). Overlap avoids cutting digits on the split line. */
    fun topHalf(src: Bitmap, overlapFraction: Float = 0.08f): Bitmap {
        val h = (src.height * (0.5f + overlapFraction)).toInt().coerceIn(1, src.height)
        return ownedCrop(src, 0, 0, src.width, h)
    }

    /** Bottom row region (MT). Overlap avoids cutting digits on the split line. */
    fun bottomHalf(src: Bitmap, overlapFraction: Float = 0.08f): Bitmap {
        val h = (src.height * (0.5f + overlapFraction)).toInt().coerceIn(1, src.height)
        return ownedCrop(src, 0, src.height - h, src.width, h)
    }

    private fun ownedCrop(src: Bitmap, x: Int, y: Int, w: Int, h: Int): Bitmap {
        val out = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        Canvas(out).drawBitmap(src, -x.toFloat(), -y.toFloat(), null)
        return out
    }
}
