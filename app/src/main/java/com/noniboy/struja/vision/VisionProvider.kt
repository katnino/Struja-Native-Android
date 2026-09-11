package com.noniboy.struja.vision

data class ExtractResult(
    val vt: Int? = null,
    val mt: Int? = null,
    val confidence: String = "low",
    val note: String? = null
)

interface VisionProvider {
    suspend fun extract(apiKey: String, base64: String, mediaType: String): ExtractResult
}
