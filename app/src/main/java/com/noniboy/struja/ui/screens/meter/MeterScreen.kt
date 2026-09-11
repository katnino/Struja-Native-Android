package com.noniboy.struja.ui.screens.meter

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.noniboy.struja.data.model.Bill
import com.noniboy.struja.data.model.Reading
import com.noniboy.struja.ui.components.BillBreakdownCard
import com.noniboy.struja.ui.components.DeleteMeterDialog
import com.noniboy.struja.ui.components.MonthOutlookCard
import com.noniboy.struja.ui.theme.StrujaColors

@Composable
fun MeterScreen(
    navController: NavController,
    meterId: String,
    viewModel: MeterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = StrujaColors.accentStrong)
        }
        return
    }

    val meter = uiState.meter
    if (meter == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Brojilo nije pronađeno",
                color = StrujaColors.fgDim
            )
        }
        return
    }

    if (uiState.showDeleteDialog) {
        DeleteMeterDialog(
            meterName = meter.name,
            onConfirm = { viewModel.deleteMeter() },
            onDismiss = { viewModel.hideDeleteDialog() }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "← Sva brojila",
                style = MaterialTheme.typography.bodySmall,
                color = StrujaColors.accentStrong,
                modifier = Modifier.clickable { navController.popBackStack() }
            )
        }

        item {
            Text(
                text = meter.name,
                style = MaterialTheme.typography.headlineSmall,
                color = StrujaColors.fgStrong,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        item {
            Text(
                text = "${meter.approvedKw} kW • ${meter.tariffGroup}",
                style = MaterialTheme.typography.bodySmall,
                color = StrujaColors.fgMute
            )
        }

        item {
            Button(
                onClick = { navController.navigate("meters/$meterId/readings/new") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = StrujaColors.accent
                ),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = "NOVO OČITANJE",
                    fontSize = 12.sp,
                    letterSpacing = 0.1.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (uiState.outlook != null) {
            item {
                MonthOutlookCard(outlook = uiState.outlook!!)
            }
        }

        item {
            Text(
                text = "Očitanja",
                style = MaterialTheme.typography.titleMedium,
                color = StrujaColors.fgMute,
                letterSpacing = 0.1.sp
            )
        }

        if (uiState.readings.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(StrujaColors.surface)
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Nema očitanja — dodajte prvo očitanje",
                        style = MaterialTheme.typography.bodySmall,
                        color = StrujaColors.fgDim
                    )
                }
            }
        } else {
            items(uiState.readings.reversed()) { reading ->
                ReadingRow(reading)
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Računi",
                style = MaterialTheme.typography.titleMedium,
                color = StrujaColors.fgMute,
                letterSpacing = 0.1.sp
            )
        }

        if (uiState.bills.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(StrujaColors.surface)
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (uiState.readings.size <= 1)
                            "Nema računa — račun se pravi od drugog očitanja"
                        else
                            "Nema računa",
                        style = MaterialTheme.typography.bodySmall,
                        color = StrujaColors.fgDim
                    )
                }
            }
        } else {
            items(uiState.bills) { bill ->
                BillCard(
                    bill = bill,
                    onClick = { navController.navigate("meters/$meterId/bills/${bill.id}") }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(StrujaColors.surface)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "OBRIŠI BROJILO I SVE RAČUNE",
                    style = MaterialTheme.typography.bodySmall,
                    color = StrujaColors.fgFaint,
                    modifier = Modifier
                        .clickable { viewModel.showDeleteDialog() }
                        .padding(8.dp),
                    letterSpacing = 0.1.sp
                )
            }
        }
    }
}

@Composable
private fun ReadingRow(reading: Reading) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(StrujaColors.surface)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = reading.recordedAt,
                style = MaterialTheme.typography.bodySmall,
                color = StrujaColors.fgMute
            )
            Text(
                text = "VT: ${reading.vt ?: 0} • MT: ${reading.mt ?: 0}",
                style = MaterialTheme.typography.bodyMedium,
                color = StrujaColors.fgStrong
            )
        }
        Text(
            text = reading.source.uppercase(),
            style = MaterialTheme.typography.bodySmall,
            color = if (reading.source == "ai") StrujaColors.info else StrujaColors.fgDim
        )
    }
}

@Composable
private fun BillCard(
    bill: Bill,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(StrujaColors.surface)
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${bill.periodStart} – ${bill.periodEnd}",
                style = MaterialTheme.typography.bodySmall,
                color = StrujaColors.fgMute
            )
            Text(
                text = "${formatMoney(bill.total)} KM",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = StrujaColors.fgStrong
            )
        }
        Text(
            text = "${formatKwh(bill.consumptionKwh)} kWh",
            style = MaterialTheme.typography.bodySmall,
            color = StrujaColors.fgDim,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

private fun formatMoney(value: Double): String {
    return String.format("%.2f", value)
}

private fun formatKwh(value: Double): String {
    return String.format("%.1f", value)
}
