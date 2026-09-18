package com.noniboy.struja.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noniboy.struja.ui.theme.StrujaColors
import com.noniboy.struja.vision.OcrEngine

@Composable
fun SettingsDialog(
    currentApiKey: String?,
    onSave: (String) -> Unit,
    onDismiss: () -> Unit,
    currentEngine: OcrEngine = OcrEngine.GEMINI,
    onEngineChange: (OcrEngine) -> Unit = {},
    backupBusy: Boolean = false,
    backupMessage: String? = null,
    pendingImport: com.noniboy.struja.data.backup.ImportSummary? = null,
    onExportClick: () -> Unit = {},
    onImportClick: () -> Unit = {},
    onConfirmImport: () -> Unit = {},
    onCancelImport: () -> Unit = {},
    onDismissBackupMessage: () -> Unit = {}
) {
    var apiKey by remember { mutableStateOf(currentApiKey ?: "") }
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = StrujaColors.modalBackground,
        titleContentColor = StrujaColors.fgStrong,
        textContentColor = StrujaColors.fg,
        title = {
            Text(
                text = "Postavke",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text(
                    text = "API ključ (Google Gemini)",
                    fontSize = 12.sp,
                    color = StrujaColors.fgMute,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                TextField(
                    value = apiKey,
                    onValueChange = { apiKey = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = StrujaColors.modalInputBackground,
                        unfocusedContainerColor = StrujaColors.modalInputBackground,
                        focusedTextColor = StrujaColors.fgStrong,
                        unfocusedTextColor = StrujaColors.fgStrong,
                        cursorColor = StrujaColors.accentStrong,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true,
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                        fontSize = 13.sp
                    )
                )
                Text(
                    text = "Nabavite besplatni ključ",
                    fontSize = 11.sp,
                    color = StrujaColors.linkColor,
                    modifier = Modifier.padding(top = 8.dp)
                        .clickable {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://aistudio.google.com/apikey"))
                            context.startActivity(intent)
                        }
                )
                Text(
                    text = "API key se čuva samo na uređaju.",
                    fontSize = 10.sp,
                    color = StrujaColors.fgDim,
                    modifier = Modifier.padding(top = 4.dp)
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = StrujaColors.borderStrong
                )

                Text(
                    text = "Prepoznavanje s fotografije",
                    fontSize = 12.sp,
                    color = StrujaColors.fgMute,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OcrEngineOption(
                    selected = currentEngine == OcrEngine.GEMINI,
                    title = "Cloud Gemini",
                    subtitle = "Preciznije, treba internet + ključ",
                    onClick = { onEngineChange(OcrEngine.GEMINI) }
                )
                OcrEngineOption(
                    selected = currentEngine == OcrEngine.LOCAL,
                    title = "Na uređaju (eksperimentalno)",
                    subtitle = "Offline, bez slanja slike",
                    onClick = { onEngineChange(OcrEngine.LOCAL) }
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = StrujaColors.borderStrong
                )

                Text(
                    text = "Sigurnosna kopija (brojila, očitanja, računi)",
                    fontSize = 12.sp,
                    color = StrujaColors.fgMute,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onExportClick,
                        enabled = !backupBusy && pendingImport == null,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = StrujaColors.surface2
                        )
                    ) {
                        Text(text = "Izvezi", fontSize = 12.sp, color = StrujaColors.fg)
                    }
                    Button(
                        onClick = onImportClick,
                        enabled = !backupBusy && pendingImport == null,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = StrujaColors.surface2
                        )
                    ) {
                        Text(text = "Uvezi", fontSize = 12.sp, color = StrujaColors.fg)
                    }
                }

                if (backupBusy) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.height(20.dp),
                            color = StrujaColors.accentStrong,
                            strokeWidth = 2.dp
                        )
                    }
                }

                if (pendingImport != null) {
                    Text(
                        text = "Uvezi: ${pendingImport.meters} brojila, " +
                            "${pendingImport.readings} očitanja, " +
                            "${pendingImport.bills} računa?",
                        fontSize = 12.sp,
                        color = StrujaColors.fgStrong,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onConfirmImport,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = StrujaColors.accent
                            )
                        ) {
                            Text(text = "Potvrdi", fontSize = 12.sp, color = StrujaColors.fgStrong)
                        }
                        TextButton(
                            onClick = onCancelImport,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Odustani", fontSize = 12.sp, color = StrujaColors.fgMute)
                        }
                    }
                }

                if (backupMessage != null) {
                    Text(
                        text = backupMessage,
                        fontSize = 11.sp,
                        color = StrujaColors.fgDim,
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .clickable(onClick = onDismissBackupMessage)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(apiKey) },
                enabled = apiKey.isNotBlank()
            ) {
                Text(
                    text = "Sačuvaj",
                    color = if (apiKey.isNotBlank()) StrujaColors.saveButton else StrujaColors.saveButtonDisabled,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Odustani",
                    color = StrujaColors.fgMute,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )
            }
        }
    )
}

@Composable
private fun OcrEngineOption(
    selected: Boolean,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = if (selected) "◉" else "○",
            fontSize = 14.sp,
            color = if (selected) StrujaColors.accentStrong else StrujaColors.fgDim
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = StrujaColors.fgStrong
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = StrujaColors.fgDim
            )
        }
    }
}
