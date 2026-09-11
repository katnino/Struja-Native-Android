package com.noniboy.struja.ui.screens.bill

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
import com.noniboy.struja.ui.components.BillBreakdownCard
import com.noniboy.struja.ui.theme.StrujaColors

@Composable
fun BillScreen(
    navController: NavController,
    meterId: String,
    billId: String,
    viewModel: BillViewModel = hiltViewModel()
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

    val bill = uiState.bill
    val meter = uiState.meter

    if (bill == null || meter == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Račun nije pronađen",
                color = StrujaColors.fgDim
            )
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "← Nazad na brojilo",
                style = MaterialTheme.typography.bodySmall,
                color = StrujaColors.accentStrong,
                modifier = Modifier.clickable { navController.popBackStack() }
            )
        }

        item {
            Text(
                text = "Račun za period ${bill.periodStart} – ${bill.periodEnd}",
                style = MaterialTheme.typography.headlineSmall,
                color = StrujaColors.fgStrong,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        item {
            BillBreakdownCard(billResult = com.noniboy.struja.domain.tariff.BillResult(
                blocks = bill.blocks,
                totalKwh = bill.consumptionKwh,
                mjernoMjesto = bill.mjernoMjesto,
                obracunskaSnaga = bill.obracunskaSnaga,
                serviceFee = bill.mjernoMjesto,
                totalEnergy = bill.energyCost,
                transmissionBaseCost = bill.transmissionBaseCost,
                totalTransmission = bill.totalTransmission,
                distributionBaseCost = bill.distributionBaseCost,
                totalDistribution = bill.totalDistribution,
                totalOie = bill.oieCost,
                subtotal = bill.subtotal,
                vatAmount = bill.vatAmount,
                total = bill.total,
                consumptionKwh = bill.consumptionKwh,
                isPartial = bill.isPartial
            ))
        }

        item {
            Button(
                onClick = { viewModel.generatePdf() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isGeneratingPdf,
                colors = ButtonDefaults.buttonColors(
                    containerColor = StrujaColors.surface2
                ),
                shape = RoundedCornerShape(6.dp)
            ) {
                if (uiState.isGeneratingPdf) {
                    CircularProgressIndicator(
                        modifier = Modifier.height(16.dp),
                        color = StrujaColors.fgStrong,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "PREUZMI PDF",
                        fontSize = 12.sp,
                        letterSpacing = 0.1.sp,
                        fontWeight = FontWeight.Bold,
                        color = StrujaColors.fg
                    )
                }
            }
        }

        if (uiState.savedPdfName != null) {
            item {
                Text(
                    text = "✓ Sačuvano u Preuzimanja/Struja: ${uiState.savedPdfName}",
                    style = MaterialTheme.typography.bodySmall,
                    color = StrujaColors.success,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(StrujaColors.success.copy(alpha = 0.15f))
                        .padding(12.dp)
                )
            }
        }

        if (uiState.error != null) {
            item {
                Text(
                    text = uiState.error!!,
                    style = MaterialTheme.typography.bodySmall,
                    color = StrujaColors.danger,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(StrujaColors.danger.copy(alpha = 0.15f))
                        .padding(12.dp)
                )
            }
        }
    }
}
