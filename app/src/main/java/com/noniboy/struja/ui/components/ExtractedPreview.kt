package com.noniboy.struja.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noniboy.struja.ui.theme.StrujaColors

@Composable
fun ExtractedPreview(
    vt: Int,
    mt: Int,
    confidence: String,
    note: String?,
    onVtChange: (Int) -> Unit,
    onMtChange: (Int) -> Unit,
    onDismiss: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val isHighConfidence = confidence == "high"
    val backgroundColor = if (isHighConfidence) StrujaColors.extractedHighConfidenceBg else StrujaColors.extractedLowConfidenceBg
    val borderColor = if (isHighConfidence) StrujaColors.success else StrujaColors.danger

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isHighConfidence) "✓ Očitano sa fotografije" else "⚠ Nisko povjerenje – provjerite vrijednosti prije spremanja",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = borderColor
            )
            Text(
                text = "✕",
                style = MaterialTheme.typography.bodyMedium,
                color = StrujaColors.fgDim,
                modifier = Modifier.clickable(onClick = onDismiss)
            )
        }

        if (note != null && note.isNotBlank()) {
            Text(
                text = note,
                style = MaterialTheme.typography.bodySmall,
                color = StrujaColors.fgDim,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "VT (Viša tarifa)",
                    style = MaterialTheme.typography.bodySmall,
                    color = StrujaColors.warn,
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    value = if (vt > 0) vt.toString() else "",
                    onValueChange = { value ->
                        val digits = value.filter { it.isDigit() }.take(9)
                        if (digits.isEmpty()) onVtChange(0)
                        else digits.toIntOrNull()?.let { onVtChange(it) }
                    },
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = StrujaColors.warn,
                        unfocusedBorderColor = StrujaColors.borderStrong,
                        focusedTextColor = StrujaColors.fgStrong,
                        unfocusedTextColor = StrujaColors.fgStrong,
                        cursorColor = StrujaColors.warn
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "MT (Manja tarifa)",
                    style = MaterialTheme.typography.bodySmall,
                    color = StrujaColors.info,
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    value = if (mt > 0) mt.toString() else "",
                    onValueChange = { value ->
                        val digits = value.filter { it.isDigit() }.take(9)
                        if (digits.isEmpty()) onMtChange(0)
                        else digits.toIntOrNull()?.let { onMtChange(it) }
                    },
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = StrujaColors.info,
                        unfocusedBorderColor = StrujaColors.borderStrong,
                        focusedTextColor = StrujaColors.fgStrong,
                        unfocusedTextColor = StrujaColors.fgStrong,
                        cursorColor = StrujaColors.info
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }
        }
    }
}
