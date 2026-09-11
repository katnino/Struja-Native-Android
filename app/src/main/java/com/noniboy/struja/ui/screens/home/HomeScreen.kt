package com.noniboy.struja.ui.screens.home

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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.noniboy.struja.data.model.Meter
import com.noniboy.struja.ui.theme.StrujaColors

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Moja brojila",
                style = MaterialTheme.typography.titleMedium,
                color = StrujaColors.fgMute,
                letterSpacing = 0.1.sp
            )
        }

        if (uiState.meters.isEmpty()) {
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
                        text = "Nema brojila. Dodajte novo.",
                        style = MaterialTheme.typography.bodySmall,
                        color = StrujaColors.fgDim
                    )
                }
            }
        } else {
            items(uiState.meters) { meter ->
                MeterCard(
                    meter = meter,
                    onClick = { navController.navigate("meters/${meter.id}") }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Dodaj brojilo",
                style = MaterialTheme.typography.titleMedium,
                color = StrujaColors.fgMute,
                letterSpacing = 0.1.sp
            )
        }

        item {
            AddMeterForm(
                uiState = uiState,
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun MeterCard(
    meter: Meter,
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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = meter.name,
                style = MaterialTheme.typography.bodyLarge,
                color = StrujaColors.fgStrong
            )
            Text(
                text = "${meter.approvedKw} kW",
                style = MaterialTheme.typography.bodySmall,
                color = StrujaColors.fgMute
            )
        }
        Text(
            text = meter.tariffGroup,
            style = MaterialTheme.typography.bodySmall,
            color = StrujaColors.fgDim,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddMeterForm(
    uiState: HomeUiState,
    viewModel: HomeViewModel
) {
    val approvedKwOptions = listOf(3.3, 4.4, 5.5, 6.6, 8.8, 11.0)
    var expanded = androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(StrujaColors.surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = uiState.newMeterName,
            onValueChange = { viewModel.updateNewMeterName(it) },
            label = { Text("Ime brojila") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = StrujaColors.accentStrong,
                unfocusedBorderColor = StrujaColors.borderStrong,
                focusedTextColor = StrujaColors.fgStrong,
                unfocusedTextColor = StrujaColors.fgStrong,
                cursorColor = StrujaColors.accentStrong
            ),
            singleLine = true
        )

        ExposedDropdownMenuBox(
            expanded = expanded.value,
            onExpandedChange = { expanded.value = it }
        ) {
            OutlinedTextField(
                value = "${uiState.newMeterApprovedKw} kW",
                onValueChange = {},
                readOnly = true,
                label = { Text("Odobrena snaga") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = StrujaColors.accentStrong,
                    unfocusedBorderColor = StrujaColors.borderStrong,
                    focusedTextColor = StrujaColors.fgStrong,
                    unfocusedTextColor = StrujaColors.fgStrong,
                    cursorColor = StrujaColors.accentStrong
                )
            )

            ExposedDropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                approvedKwOptions.forEach { kw ->
                    DropdownMenuItem(
                        text = { Text("$kw kW") },
                        onClick = {
                            viewModel.updateNewMeterApprovedKw(kw)
                            expanded.value = false
                        }
                    )
                }
            }
        }

        if (uiState.error != null) {
            Text(
                text = uiState.error!!,
                style = MaterialTheme.typography.bodySmall,
                color = StrujaColors.danger
            )
        }

        Button(
            onClick = { viewModel.saveMeter() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = StrujaColors.accent
            ),
            shape = RoundedCornerShape(6.dp)
        ) {
            Text(
                text = "SAČUVAJ BROJILO",
                fontSize = 12.sp,
                letterSpacing = 0.1.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
