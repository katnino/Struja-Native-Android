package com.noniboy.struja.vision

import android.content.Context

/**
 * User-chosen OCR engine (Settings-level, persisted).
 *
 * GEMINI = current cloud path (default, preserves existing behavior).
 * LOCAL  = on-device ML Kit path (offline, experimental, strict no-fallback).
 */
enum class OcrEngine(val prefValue: String) {
    GEMINI("gemini"),
    LOCAL("local");

    companion object {
        const val PREF_KEY = "ocr_engine"

        fun fromPref(value: String?): OcrEngine {
            return entries.firstOrNull { it.prefValue == value } ?: GEMINI
        }

        fun read(context: Context): OcrEngine {
            val prefs = context.getSharedPreferences("struja_settings", Context.MODE_PRIVATE)
            return fromPref(prefs.getString(PREF_KEY, null))
        }

        fun save(context: Context, engine: OcrEngine) {
            context.getSharedPreferences("struja_settings", Context.MODE_PRIVATE)
                .edit().putString(PREF_KEY, engine.prefValue).apply()
        }
    }
}
