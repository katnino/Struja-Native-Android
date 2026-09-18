package com.noniboy.struja.vision

/**
 * Pure, testable post-processing for on-device OCR.
 *
 * Problem: ML Kit reads *everything* (serials, specs, red decimal),
 * while Gemini understands layout from the prompt. This replicates the
 * layout rules geometrically, without any network:
 *  top row = VT, bottom row = MT, 5 main digits, ignore serials below disc.
 *
 * No Android dependencies — safe for JVM unit tests.
 */
data class OcrLine(
    val text: String,
    /** Vertical center in the same space for all lines (px). */
    val centerY: Float,
    /** Box height in the same space (px). Used to drop small serial/spec print. */
    val boxHeight: Float
)

private data class Candidate(
    val value: Int,
    val digits: Int,
    val centerY: Float,
    val boxHeight: Float,
    val strippedRedDecimal: Boolean
)

object MeterOcrParser {

    fun parse(lines: List<OcrLine>, imageHeight: Float? = null): ExtractResult {
        if (lines.isEmpty()) {
            return ExtractResult(
                confidence = "low",
                note = "Uređaj nije pronašao tekst na slici. Približite cifre."
            )
        }

        // 1. Extract digit-group candidates per line.
        val candidates = lines.flatMap { line -> candidatesFromLine(line) }
        if (candidates.isEmpty()) {
            return ExtractResult(
                confidence = "low",
                note = "Uređaj nije pronašao očitanje. Provjerite vrijednosti."
            )
        }

        // 2. Drop small print (serials/specs) relative to the largest digits.
        // Relative threshold keeps this resolution-independent.
        val maxHeight = candidates.maxOf { it.boxHeight }.coerceAtLeast(1f)
        val sized = candidates.filter { it.boxHeight >= maxHeight * 0.5f }
        if (sized.isEmpty()) {
            return ExtractResult(
                confidence = "low",
                note = "Pronađen je samo sitan tekst (serijski brojevi). Približite cifre."
            )
        }

        // 3. Drop anything below the rotating disc (bottom 30% when height known).
        val inArea = if (imageHeight != null && imageHeight > 0) {
            sized.filter { it.centerY <= imageHeight * 0.72f }
        } else {
            sized
        }
        if (inArea.isEmpty()) {
            return ExtractResult(
                confidence = "low",
                note = "Pronađeni su samo brojevi ispod diska (serijski). Slikajte cifre."
            )
        }

        // 4. Split into top (VT) / bottom (MT) clusters via largest vertical gap.
        val sorted = inArea.sortedBy { it.centerY }
        val splitIndex = largestGapSplit(sorted)
        val top = sorted.subList(0, splitIndex)
        val bottom = sorted.subList(splitIndex, sorted.size)

        val vt = pickBest(top)
        val mt = pickBest(bottom)

        // Single-row fallback: only one cluster found (e.g. one row readable).
        if (vt == null && mt == null) {
            return ExtractResult(
                confidence = "low",
                note = "Nejasno očitanje — provjerite vrijednosti prije spremanja."
            )
        }

        val bothFiveDigits = (vt?.digits == 5) && (mt?.digits == 5)
        val separated = if (vt != null && mt != null) {
            kotlin.math.abs(vt.centerY - mt.centerY) >= maxHeight * 0.8f
        } else false

        return if (vt != null && mt != null && bothFiveDigits && separated) {
            ExtractResult(vt = vt.value, mt = mt.value, confidence = "high")
        } else {
            ExtractResult(
                vt = vt?.value,
                mt = mt?.value,
                confidence = "low",
                note = "Nisko povjerenje (uređaj) — provjerite vrijednosti prije spremanja."
            )
        }
    }

    /** Best single value in these lines (for one pre-split row region). Null = nothing usable. */
    fun bestSingle(lines: List<OcrLine>): Int? {
        val all = lines.flatMap { candidatesFromLine(it) }
        if (all.isEmpty()) return null
        val maxHeight = all.maxOf { it.boxHeight }.coerceAtLeast(1f)
        return pickBest(all.filter { it.boxHeight >= maxHeight * 0.5f })?.value
    }

    private fun candidatesFromLine(line: OcrLine): List<Candidate> {
        val groups = Regex("\\d+").findAll(line.text).map { it.value }.toList()
        val out = mutableListOf<Candidate>()
        // Joined run first: ML Kit often splits the row around the red decimal
        // ("8234 56"). Joined 5-6 chars = 5 mains (+ red) -> take first 5.
        // 7+ chars = serials/specs glued together -> ignored here, per-group below still applies.
        val joined = groups.joinToString("")
        if (joined.length in 5..6) {
            joined.take(5).toIntOrNull()?.let {
                out.add(Candidate(it, 5, line.centerY, line.boxHeight, joined.length == 6))
            }
        }
        for (g in groups) {
            when {
                g.length in 4..5 -> {
                    g.toIntOrNull()?.let {
                        out.add(Candidate(it, g.length, line.centerY, line.boxHeight, false))
                    }
                }
                g.length == 6 -> {
                    // 5 main digits + 1 red decimal slightly spaced — strip the last.
                    g.take(5).toIntOrNull()?.let {
                        out.add(Candidate(it, 5, line.centerY, line.boxHeight, true))
                    }
                }
                // len <= 3: noise/spec fragments — ignore.
                // len >= 7: serial numbers / model info — ignore.
                else -> Unit
            }
        }
        return out
    }

    /** Index where the largest Y gap occurs; splits into top/bottom clusters. */
    private fun largestGapSplit(sorted: List<Candidate>): Int {
        if (sorted.size < 2) return sorted.size
        var bestIndex = sorted.size
        var bestGap = 0f
        for (i in 1 until sorted.size) {
            val gap = sorted[i].centerY - sorted[i - 1].centerY
            if (gap > bestGap) {
                bestGap = gap
                bestIndex = i
            }
        }
        // If all candidates sit on nearly the same row, keep them as one cluster.
        val rowTolerance = sorted.maxOf { it.boxHeight }.coerceAtLeast(1f) * 0.8f
        return if (bestGap < rowTolerance) sorted.size else bestIndex
    }

    /** Prefer 5-digit, then taller box (bigger print = meter digits, not specs). */
    private fun pickBest(cluster: List<Candidate>): Candidate? {
        return cluster.sortedWith(
            compareByDescending<Candidate> { it.digits == 5 }
                .thenByDescending { it.boxHeight }
                .thenByDescending { it.digits }
        ).firstOrNull()
    }
}
