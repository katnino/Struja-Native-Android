package com.noniboy.struja.vision

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiProvider @Inject constructor(
    private val okHttpClient: OkHttpClient
) : VisionProvider {

    override suspend fun extract(apiKey: String, base64: String, mediaType: String): ExtractResult =
        withContext(Dispatchers.IO) {
        val prompt = buildPrompt()

        val requestBody = """
        {
            "contents": [{
                "parts": [
                    {"inlineData": {"mimeType": "$mediaType", "data": "$base64"}},
                    {"text": ${escapeJsonString(prompt)}}
                ]
            }],
            "generationConfig": {
                "temperature": 0,
                "response_mime_type": "application/json"
            }
        }
        """.trimIndent()

        val request = Request.Builder()
            .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey")
            .addHeader("Content-Type", "application/json")
            .post(requestBody.toRequestBody("application/json".toMediaType()))
            .build()

        return@withContext try {
            okHttpClient.newCall(request).execute().use { response ->
                val body = response.body?.string() ?: throw Exception("Empty response")

                if (!response.isSuccessful) {
                    return@use ExtractResult(
                        confidence = "low",
                        note = "API greška: ${response.code} - ${body.take(500)}"
                    )
                }

                val jsonResponse = try {
                    JsonParser.parseString(body).asJsonObject
                } catch (e: Exception) {
                    return@use ExtractResult(
                        confidence = "low",
                        note = "Greška pri ekstrakciji: neispravan odgovor servera"
                    )
                }
                val candidates = if (jsonResponse.has("candidates") && jsonResponse.get("candidates").isJsonArray) {
                    jsonResponse.getAsJsonArray("candidates")
                } else null
                if (candidates == null || candidates.size() == 0) {
                    return@use ExtractResult(
                        confidence = "low",
                        note = "Greška pri ekstrakciji: prazan odgovor"
                    )
                }
                val content = candidates.get(0)?.asJsonObject?.getAsJsonObject("content")
                val parts = content?.getAsJsonArray("parts")
                if (parts == null || parts.size() == 0) {
                    return@use ExtractResult(
                        confidence = "low",
                        note = "Greška pri ekstrakciji: prazan odgovor"
                    )
                }
                val textElement = parts.get(0)?.asJsonObject?.get("text")
                if (textElement == null || textElement.isJsonNull) {
                    return@use ExtractResult(
                        confidence = "low",
                        note = "Greška pri ekstrakciji: prazan odgovor"
                    )
                }
                val text = textElement.asString

                parseResponse(text)
            }
        } catch (e: Exception) {
            ExtractResult(
                confidence = "low",
                note = "Greška pri ekstrakciji: ${e.message}"
            )
        }
    }

    private fun parseResponse(text: String): ExtractResult {
        return try {
            val cleanText = text
                .replace(Regex("```(?:json)?"), "")
                .replace("```", "")
                .trim()

            val json = JsonParser.parseString(cleanText).asJsonObject
            ExtractResult(
                vt = parseMeterValue(json, "vt"),
                mt = parseMeterValue(json, "mt"),
                confidence = json.get("confidence")?.takeIf { it.isJsonPrimitive }?.asString ?: "low",
                note = json.get("note")?.takeIf { it.isJsonPrimitive }?.asString
            )
        } catch (e: Exception) {
            ExtractResult(
                confidence = "low",
                note = "Gemini returned unparseable JSON: ${text.take(300)}"
            )
        }
    }

    private fun parseMeterValue(json: JsonObject, key: String): Int? {
        val el = json.get(key) ?: return null
        if (el.isJsonNull) return null
        if (!el.isJsonPrimitive) return null
        val prim = el.asJsonPrimitive
        return try {
            if (prim.isNumber) {
                val d = prim.asDouble
                if (!d.isFinite() || d < 0) null else Math.floor(d).toInt()
            } else if (prim.isString) {
                prim.asString.filter { it.isDigit() }.take(9).toIntOrNull()
            } else null
        } catch (e: Exception) {
            null
        }
    }

    private fun escapeJsonString(s: String): String {
        return "\"" + s
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t") + "\""
    }

    private fun buildPrompt(): String {
        return """You are looking at a dual-tariff (dvotarifna) electricity meter.

LAYOUT — Two rows of digit wheels, one below the other, with "kWh" written between them:

  TOP ROW    = VT (Viša tarifa / peak)
  BOTTOM ROW = MT (Manja tarifa / off-peak)

Each row has 6 digit wheels:
  • 5 white/black main digits (the actual meter reading)
  • 1 red decimal digit slightly spaced to the right — IGNORE THIS

Extract ONLY the 5 main digits from each row as whole numbers (e.g. 82345, NOT 82345.6).

IGNORE all numbers below the rotating disc / impulse wheel (those are serial numbers, specs, model info — not meter readings).

Return JSON:
{"vt": 82345, "mt": 52341, "confidence": "high", "note": "optional note"}
If unclear, set confidence to "low" and explain which number was ambiguous."""
    }
}
