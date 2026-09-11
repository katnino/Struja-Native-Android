package com.noniboy.struja.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noniboy.struja.ui.theme.StrujaColors

@Composable
fun DeleteMeterDialog(
    meterName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = StrujaColors.modalBackground,
        titleContentColor = StrujaColors.fgStrong,
        textContentColor = StrujaColors.fg,
        title = {
            Text(
                text = "⚠ Obriši brojilo",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "Da li ste sigurni da želite obrisati brojilo \"$meterName\" i sve njegove račune? Ova akcija se ne može poništiti.",
                fontSize = 14.sp,
                color = StrujaColors.fg
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = "Obriši",
                    color = StrujaColors.danger,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Otkaži",
                    color = StrujaColors.fgMute,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )
            }
        }
    )
}
