package com.noniboy.struja.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noniboy.struja.data.model.BlockBreakdown
import com.noniboy.struja.domain.tariff.BillResult
import com.noniboy.struja.ui.theme.StrujaColors

@Composable
fun BillBreakdownCard(
    billResult: BillResult,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(StrujaColors.bg.copy(alpha = 0.3f))
            .padding(20.dp)
    ) {
        Text(
            text = "📄 Obračun — ${formatKwh(billResult.totalKwh)} kWh",
            style = MaterialTheme.typography.titleMedium,
            color = StrujaColors.fgStrong,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        billResult.blocks.forEach { block ->
            BillBlockRow(block)
        }

        HorizontalDivider()

        BillRow(label = "Mjerno mjesto", value = "${formatMoney(billResult.mjernoMjesto)} KM",
            isExcluded = billResult.isPartial)
        BillRow(label = "Aktivna energija", value = "${formatMoney(billResult.totalEnergy)} KM")

        if (billResult.transmissionBaseCost > 0) {
            BillRow(
                label = "Prenosna mrežarina",
                value = "${formatMoney(billResult.totalTransmission)} KM",
                hint = "${formatMoney(billResult.transmissionBaseCost)} KM po kWh + ${formatMoney(billResult.totalTransmission - billResult.transmissionBaseCost)} KM po kW"
            )
        } else {
            BillRow(label = "Prenosna mrežarina", value = "${formatMoney(billResult.totalTransmission)} KM")
        }

        if (billResult.distributionBaseCost > 0) {
            BillRow(
                label = "Distributivna mrežarina",
                value = "${formatMoney(billResult.totalDistribution)} KM",
                hint = "${formatMoney(billResult.distributionBaseCost)} KM po kWh + ${formatMoney(billResult.totalDistribution - billResult.distributionBaseCost)} KM po kW"
            )
        } else {
            BillRow(label = "Distributivna mrežarina", value = "${formatMoney(billResult.totalDistribution)} KM")
        }

        BillRow(label = "Naknada OIE", value = "${formatMoney(billResult.totalOie)} KM")

        if (!billResult.isPartial) {
            HorizontalDivider()
            BillRow(label = "Osnovica (bez PDV)", value = "${formatMoney(billResult.subtotal)} KM")
            BillRow(label = "PDV (17%)", value = "${formatMoney(billResult.vatAmount)} KM")
        } else {
            Text(
                text = "* Mjerno mjesto, Obračunska snaga i PDV nisu uključeni u ukupan iznos (period kraći od 29 dana).",
                style = MaterialTheme.typography.bodySmall,
                color = StrujaColors.danger,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        HorizontalDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "UKUPNO SA PDV",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = StrujaColors.accentStrong
            )
            Text(
                text = "${formatMoney(billResult.total)} KM",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = StrujaColors.fgStrong
            )
        }
    }
}

@Composable
private fun BillBlockRow(block: BlockBreakdown) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = block.label,
            style = MaterialTheme.typography.bodySmall,
            color = StrujaColors.fgMute,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "${formatMoney(block.kwh)} kWh x ${formatMoney(block.rate)} = ${formatMoney(block.totalCost)} KM",
            style = MaterialTheme.typography.bodySmall,
            color = StrujaColors.fg
        )
    }
}

@Composable
private fun BillRow(
    label: String,
    value: String,
    hint: String? = null,
    isExcluded: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = if (isExcluded) StrujaColors.danger else StrujaColors.fgMute
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                color = if (isExcluded) StrujaColors.danger else StrujaColors.fg
            )
        }
        if (hint != null) {
            Text(
                text = hint,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                color = StrujaColors.fgDim,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
private fun HorizontalDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(StrujaColors.surface2)
            .padding(vertical = 8.dp)
    )
}

private fun formatMoney(value: Double): String {
    return String.format("%.2f", value)
}

private fun formatKwh(value: Double): String {
    return String.format("%.2f", value)
}
