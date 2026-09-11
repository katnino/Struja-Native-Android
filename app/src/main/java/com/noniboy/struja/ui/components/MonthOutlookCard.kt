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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.noniboy.struja.domain.outlook.Confidence
import com.noniboy.struja.domain.outlook.MonthOutlook
import com.noniboy.struja.ui.theme.StrujaColors

@Composable
fun MonthOutlookCard(
    outlook: MonthOutlook,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(StrujaColors.surface)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Trenutni mjesec",
                style = MaterialTheme.typography.titleMedium,
                color = StrujaColors.fgStrong
            )
            Text(
                text = "${outlook.month}/${outlook.year}",
                style = MaterialTheme.typography.bodyMedium,
                color = StrujaColors.fgMute
            )
        }

        if (outlook.actual != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "VT",
                        style = MaterialTheme.typography.bodySmall,
                        color = StrujaColors.warn
                    )
                    Text(
                        text = "${formatKwh(outlook.actual.vtKwh)} kWh",
                        style = MaterialTheme.typography.bodyMedium,
                        color = StrujaColors.fgStrong
                    )
                }
                Column {
                    Text(
                        text = "MT",
                        style = MaterialTheme.typography.bodySmall,
                        color = StrujaColors.info
                    )
                    Text(
                        text = "${formatKwh(outlook.actual.mtKwh)} kWh",
                        style = MaterialTheme.typography.bodyMedium,
                        color = StrujaColors.fgStrong
                    )
                }
                Column {
                    Text(
                        text = "Dani",
                        style = MaterialTheme.typography.bodySmall,
                        color = StrujaColors.fgMute
                    )
                    Text(
                        text = "${outlook.actual.daysElapsed}/${outlook.daysInMonth}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = StrujaColors.fgStrong
                    )
                }
            }
        }

        if (outlook.runRate != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Prosjek dnevno",
                    style = MaterialTheme.typography.bodySmall,
                    color = StrujaColors.fgDim
                )
                Text(
                    text = "VT: ${formatKwh(outlook.runRate.vtPerDay)} | MT: ${formatKwh(outlook.runRate.mtPerDay)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = StrujaColors.fgDim
                )
            }
        }

        if (outlook.bill != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(StrujaColors.surface2)
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Procijena",
                            style = MaterialTheme.typography.bodySmall,
                            color = StrujaColors.fgMute
                        )
                        Text(
                            text = "${formatKwh(outlook.totalEstimatedVt + outlook.totalEstimatedMt)} kWh",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = StrujaColors.fgStrong
                        )
                    }
                    Column(horizontalAlignment = androidx.compose.ui.Alignment.End) {
                        Text(
                            text = "Račun",
                            style = MaterialTheme.typography.bodySmall,
                            color = StrujaColors.fgMute
                        )
                        Text(
                            text = "${formatMoney(outlook.bill.total)} KM",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = getConfidenceColor(outlook.confidence)
                        )
                    }
                }
            }
        }

        Text(
            text = getConfidenceText(outlook.confidence),
            style = MaterialTheme.typography.bodySmall,
            color = StrujaColors.fgDim,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
private fun getConfidenceColor(confidence: Confidence): androidx.compose.ui.graphics.Color {
    return when (confidence) {
        Confidence.MEASURED -> StrujaColors.success
        Confidence.PROJECTED -> StrujaColors.fgStrong
        Confidence.INSUFFICIENT_DATA -> StrujaColors.fgDim
    }
}

private fun getConfidenceText(confidence: Confidence): String {
    return when (confidence) {
        Confidence.MEASURED -> "📊 Izmjereno"
        Confidence.PROJECTED -> "📈 Procijena"
        Confidence.INSUFFICIENT_DATA -> "⚠ Nedovoljno podataka"
    }
}

private fun formatKwh(value: Double): String {
    return String.format("%.1f", value)
}

private fun formatMoney(value: Double): String {
    return String.format("%.2f", value)
}
