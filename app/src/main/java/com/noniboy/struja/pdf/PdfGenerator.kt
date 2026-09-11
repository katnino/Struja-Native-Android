package com.noniboy.struja.pdf

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.core.content.FileProvider
import com.noniboy.struja.data.model.BlockBreakdown
import com.noniboy.struja.domain.tariff.BillResult
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PdfGenerator @Inject constructor() {

    fun generate(
        context: Context,
        meterName: String,
        periodStart: String,
        periodEnd: String,
        blocks: List<BlockBreakdown>,
        billResult: BillResult,
        fileSuffix: String? = null
    ): Uri {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = document.startPage(pageInfo)
        val canvas = page.canvas

        val titlePaint = Paint().apply {
            color = Color.parseColor("#3B82F6")
            textSize = 28f
            typeface = Typeface.create("serif", Typeface.BOLD)
            isAntiAlias = true
        }

        val headerPaint = Paint().apply {
            color = Color.parseColor("#1E293B")
            textSize = 14f
            typeface = Typeface.DEFAULT_BOLD
            isAntiAlias = true
        }

        val bodyPaint = Paint().apply {
            color = Color.parseColor("#1E293B")
            textSize = 11f
            typeface = Typeface.DEFAULT
            isAntiAlias = true
        }

        val bodyBoldPaint = Paint().apply {
            color = Color.parseColor("#1E293B")
            textSize = 11f
            typeface = Typeface.DEFAULT_BOLD
            isAntiAlias = true
        }

        val mutedPaint = Paint().apply {
            color = Color.parseColor("#94A3B8")
            textSize = 10f
            typeface = Typeface.DEFAULT
            isAntiAlias = true
        }

        val hintPaint = Paint().apply {
            color = Color.parseColor("#6E6E6E")
            textSize = 9f
            typeface = Typeface.DEFAULT
            isAntiAlias = true
        }

        val dangerPaint = Paint().apply {
            color = Color.parseColor("#EF4444")
            textSize = 11f
            typeface = Typeface.DEFAULT
            isAntiAlias = true
        }

        val dangerBoldPaint = Paint().apply {
            color = Color.parseColor("#EF4444")
            textSize = 11f
            typeface = Typeface.DEFAULT_BOLD
            isAntiAlias = true
        }

        val linePaint = Paint().apply {
            color = Color.parseColor("#C8C8C8")
            strokeWidth = 1f
        }

        val totalPaint = Paint().apply {
            color = Color.parseColor("#3B82F6")
            textSize = 16f
            typeface = Typeface.DEFAULT_BOLD
            isAntiAlias = true
        }

        val pageW = 515f  // 595 - 2*40 margins
        val margin = 40f
        var y = 40f

        // Header
        canvas.drawText("Struja", margin, y + 28f, titlePaint)
        y += 40f
        canvas.drawText("$meterName · $periodStart - $periodEnd", margin, y + 10f, mutedPaint)
        y += 16f

        // Consumption
        canvas.drawText("Obračun — ${formatKwh(billResult.totalKwh)} kWh", margin, y + 14f, headerPaint)
        y += 20f

        // Line
        canvas.drawLine(margin, y, margin + pageW, y, linePaint)
        y += 6f

        // Blocks
        for (block in blocks) {
            canvas.drawText("• ${block.label}", margin, y + 10f, bodyPaint)
            canvas.drawText(
                "${formatKwh(block.kwh)} kWh × ${formatRate(block.rate)} = ${formatMoney(block.activeEnergyCost)} KM",
                margin + pageW, y + 10f, bodyPaint.apply { textAlign = Paint.Align.RIGHT }
            )
            bodyPaint.textAlign = Paint.Align.LEFT
            y += 16f
        }

        if (blocks.isNotEmpty()) y += 2f

        // Line
        canvas.drawLine(margin, y, margin + pageW, y, linePaint)
        y += 6f

        // Mjerno mjesto
        if (billResult.isPartial) {
            drawRow(canvas, margin, pageW, y, "Mjerno mjesto", "${formatMoney(billResult.mjernoMjesto)} KM", dangerBoldPaint)
        } else {
            drawRow(canvas, margin, pageW, y, "Mjerno mjesto", "${formatMoney(billResult.mjernoMjesto)} KM", bodyBoldPaint)
        }
        y += 16f

        // Aktivna energija
        drawRow(canvas, margin, pageW, y, "Aktivna energija", "${formatMoney(billResult.totalEnergy)} KM", bodyBoldPaint)
        y += 16f

        // Prenosna mrežarina
        drawRow(canvas, margin, pageW, y, "Prenosna mrežarina", "${formatMoney(billResult.totalTransmission)} KM", bodyBoldPaint)
        y += 14f
        canvas.drawText(
            "${formatMoney(billResult.transmissionBaseCost)} KM po kWh + ${formatMoney(billResult.totalTransmission - billResult.transmissionBaseCost)} KM po kW",
            margin + pageW, y, hintPaint.apply { textAlign = Paint.Align.RIGHT }
        )
        hintPaint.textAlign = Paint.Align.LEFT
        y += 12f

        // Distributivna mrežarina
        drawRow(canvas, margin, pageW, y, "Distributivna mrežarina", "${formatMoney(billResult.totalDistribution)} KM", bodyBoldPaint)
        y += 14f
        canvas.drawText(
            "${formatMoney(billResult.distributionBaseCost)} KM po kWh + ${formatMoney(billResult.totalDistribution - billResult.distributionBaseCost)} KM po kW",
            margin + pageW, y, hintPaint.apply { textAlign = Paint.Align.RIGHT }
        )
        hintPaint.textAlign = Paint.Align.LEFT
        y += 12f

        // Naknada OIE
        drawRow(canvas, margin, pageW, y, "Naknada OIE", "${formatMoney(billResult.totalOie)} KM", bodyBoldPaint)
        y += 16f

        if (billResult.isPartial) {
            canvas.drawText(
                "* Mjerno mjesto, Obračunska snaga i PDV nisu uključeni u ukupan iznos (period kraći od 29 dana).",
                margin, y + 10f, dangerPaint
            )
            y += 16f
        } else {
            drawRow(canvas, margin, pageW, y, "Osnovica (bez PDV)", "${formatMoney(billResult.subtotal)} KM", mutedPaint)
            y += 16f
            drawRow(canvas, margin, pageW, y, "PDV (17%)", "${formatMoney(billResult.vatAmount)} KM", mutedPaint)
            y += 16f
        }

        // Line
        canvas.drawLine(margin, y, margin + pageW, y, linePaint)
        y += 12f

        // Total
        canvas.drawText("UKUPNO SA PDV", margin, y + 16f, totalPaint)
        canvas.drawText(
            "${formatMoney(billResult.total)} KM",
            margin + pageW, y + 16f, totalPaint.apply { textAlign = Paint.Align.RIGHT }
        )
        y += 40f

        // Footer
        canvas.drawText(
            "REERS odluka 17.12.2024 · primjena od 01.06.2026. · informativni obračun",
            margin, 820f, mutedPaint
        )

        document.finishPage(page)

        // Save to cache directory (unique, sanitized; MediaStore copy is made by the caller)
        val safeMeter = meterName.replace(Regex("[^a-zA-Z0-9-_]+"), "_").take(40).ifBlank { "racun" }
        val safeSuffix = fileSuffix?.replace(Regex("[^a-zA-Z0-9-_]+"), "_")?.take(16)
        val fileName = buildString {
            append("Struja-").append(safeMeter)
            append("-").append(periodStart).append("_").append(periodEnd)
            if (!safeSuffix.isNullOrBlank()) append("-").append(safeSuffix)
            append(".pdf")
        }
        val file = File(context.cacheDir, fileName)
        try {
            FileOutputStream(file).use { outputStream ->
                document.writeTo(outputStream)
            }
        } finally {
            document.close()
        }

        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }

    private fun drawRow(canvas: Canvas, margin: Float, pageW: Float, y: Float, label: String, value: String, paint: Paint) {
        canvas.drawText(label, margin, y + 12f, paint)
        canvas.drawText(value, margin + pageW, y + 12f, paint.apply { textAlign = Paint.Align.RIGHT })
        paint.textAlign = Paint.Align.LEFT
    }

    private fun formatMoney(value: Double): String {
        return String.format("%.2f", value)
    }

    private fun formatKwh(value: Double): String {
        return String.format("%.2f", value)
    }

    private fun formatRate(value: Double): String {
        return String.format("%.4f", value)
    }
}
