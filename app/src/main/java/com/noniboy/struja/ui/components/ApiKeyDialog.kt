package com.noniboy.struja.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
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

@Composable
fun ApiKeyDialog(
    onSave: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var apiKey by remember { mutableStateOf("") }
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = StrujaColors.modalBackground,
        titleContentColor = StrujaColors.fgStrong,
        textContentColor = StrujaColors.fg,
        title = {
            Text(
                text = "Potreban API ključ",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text(
                    text = "Za prepoznavanje očitanja brojila potreban je Google Gemini API ključ. Čuva se samo na ovom uređaju i nikada se ne šalje na server ove aplikacije.",
                    fontSize = 13.sp,
                    color = StrujaColors.fgMute,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Text(
                    text = "Google Gemini API Key",
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
                    ),
                    placeholder = {
                        Text(
                            text = "AIzaSy...",
                            color = StrujaColors.fgFaint,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                            fontSize = 13.sp
                        )
                    }
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
