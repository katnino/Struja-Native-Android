package com.noniboy.struja.ui.screens.reading

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.noniboy.struja.ui.components.ApiKeyDialog
import com.noniboy.struja.ui.components.BillBreakdownCard
import com.noniboy.struja.ui.components.ExtractedPreview
import com.noniboy.struja.ui.theme.StrujaColors
import java.io.File
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingScreen(
    navController: NavController,
    meterId: String,
    viewModel: ReadingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }
    var showApiKeyDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { viewModel.onImagePicked(it) }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) pendingCameraUri?.let { viewModel.onImagePicked(it) }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted: Boolean ->
        if (granted) {
            val uri = createCameraImageUri(context)
            pendingCameraUri = uri
            cameraLauncher.launch(uri)
        }
    }

    fun launchCamera() {
        val permission = Manifest.permission.CAMERA
        if (ContextCompat.checkSelfPermission(context, permission) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            val uri = createCameraImageUri(context)
            pendingCameraUri = uri
            cameraLauncher.launch(uri)
        } else {
            cameraPermissionLauncher.launch(permission)
        }
    }

    if (uiState.success) {
        LaunchedEffect(Unit) {
            navController.popBackStack()
        }
        return
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = try {
                LocalDate.parse(uiState.recordedAt)
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()
            } catch (e: Exception) {
                System.currentTimeMillis()
            }
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        viewModel.updateRecordedAt(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Otkaži")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showApiKeyDialog) {
        ApiKeyDialog(
            onSave = { key ->
                val prefs = context.getSharedPreferences("struja_settings", android.content.Context.MODE_PRIVATE)
                prefs.edit().putString("gemini_api_key", key).apply()
                showApiKeyDialog = false
            },
            onDismiss = { showApiKeyDialog = false }
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
                text = "← Nazad na brojilo",
                style = MaterialTheme.typography.bodySmall,
                color = StrujaColors.accentStrong,
                modifier = Modifier.clickable { navController.popBackStack() }
            )
        }

        item {
            Text(
                text = "Novo očitanje",
                style = MaterialTheme.typography.headlineSmall,
                color = StrujaColors.fgStrong,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        item {
            ModeSelector(
                mode = uiState.mode,
                onModeChange = { viewModel.updateMode(it) }
            )
        }

        if (uiState.mode == "manual") {
            item {
                ReadingForm(
                    vt = uiState.vt,
                    mt = uiState.mt,
                    recordedAt = uiState.recordedAt,
                    onVtChange = { viewModel.updateVt(it) },
                    onMtChange = { viewModel.updateMt(it) },
                    onDateClick = { showDatePicker = true }
                )
            }
        } else {
            item {
                ImageCaptureSection(
                    pickedImageUri = uiState.pickedImageUri,
                    isExtracting = uiState.isExtracting,
                    onPickImage = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    onTakePhoto = { launchCamera() },
                    onExtract = {
                        val prefs = context.getSharedPreferences("struja_settings", android.content.Context.MODE_PRIVATE)
                        val apiKey = prefs.getString("gemini_api_key", "") ?: ""
                        if (apiKey.isBlank()) {
                            showApiKeyDialog = true
                        } else {
                            viewModel.extractReadingFromImage()
                        }
                    }
                )
            }
            item {
                ReadingDateRow(
                    recordedAt = uiState.recordedAt,
                    onDateClick = { showDatePicker = true }
                )
            }
        }

        if (uiState.extractedResult != null) {
            item {
                ExtractedPreview(
                    vt = uiState.extractedResult!!.vt ?: 0,
                    mt = uiState.extractedResult!!.mt ?: 0,
                    confidence = uiState.extractedResult!!.confidence,
                    note = uiState.extractedResult!!.note,
                    onVtChange = { viewModel.updateVt(it.toString()) },
                    onMtChange = { viewModel.updateMt(it.toString()) },
                    onDismiss = { viewModel.dismissExtractedResult() }
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

        if (uiState.previewBill != null) {
            item {
                BillBreakdownCard(billResult = uiState.previewBill!!)
            }
        }

        item {
            Button(
                onClick = { viewModel.saveReading() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isSaving && uiState.vt.isNotBlank() && uiState.mt.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = StrujaColors.accent,
                    disabledContainerColor = StrujaColors.accent.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(6.dp)
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.height(16.dp),
                        color = StrujaColors.fgStrong,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "SPREMI OČITANJE (I RAČUN)",
                        fontSize = 12.sp,
                        letterSpacing = 0.1.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun ImageCaptureSection(
    pickedImageUri: Uri?,
    isExtracting: Boolean,
    onPickImage: () -> Unit,
    onTakePhoto: () -> Unit,
    onExtract: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(StrujaColors.surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (pickedImageUri != null) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(pickedImageUri)
                    .crossfade(true)
                    .build(),
                contentDescription = "Slika brojila",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(StrujaColors.bg)
                    .clickable(onClick = onPickImage),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "📷",
                        fontSize = 32.sp
                    )
                    Text(
                        text = "Prevucite fotografiju ili kliknite za odabir",
                        style = MaterialTheme.typography.bodySmall,
                        color = StrujaColors.fgDim
                    )
                    Text(
                        text = "JPG, PNG, WEBP",
                        style = MaterialTheme.typography.bodySmall,
                        color = StrujaColors.fgFaint,
                        fontSize = 10.sp
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onPickImage,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = StrujaColors.surface2
                ),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = if (pickedImageUri != null) "Promijeni sliku" else "Izaberi iz galerije",
                    fontSize = 12.sp,
                    color = StrujaColors.fg
                )
            }

            Button(
                onClick = onTakePhoto,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = StrujaColors.surface2
                ),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = "📷 Slikaj kamerom",
                    fontSize = 12.sp,
                    color = StrujaColors.fg
                )
            }
        }

        if (pickedImageUri != null) {
            Button(
                onClick = onExtract,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isExtracting,
                colors = ButtonDefaults.buttonColors(
                    containerColor = StrujaColors.accent
                ),
                shape = RoundedCornerShape(6.dp)
            ) {
                if (isExtracting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = StrujaColors.fgStrong,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "🔍 Ekstraktuj očitavanje (AI)",
                        fontSize = 12.sp,
                        color = StrujaColors.fgStrong
                    )
                }
            }
        }

        if (isExtracting) {
            Text(
                text = "⏳ Analiziram fotografiju...",
                style = MaterialTheme.typography.bodySmall,
                color = StrujaColors.fgMute
            )
        }
    }
}

@Composable
private fun ModeSelector(
    mode: String,
    onModeChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(StrujaColors.surface)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        ModeButton(
            text = "✏ Ručni unos",
            isSelected = mode == "manual",
            onClick = { onModeChange("manual") },
            modifier = Modifier.weight(1f)
        )
        ModeButton(
            text = "📷 Fotografija",
            isSelected = mode == "ai",
            onClick = { onModeChange("ai") },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ModeButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(if (isSelected) StrujaColors.accent else StrujaColors.bg)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = if (isSelected) StrujaColors.fgStrong else StrujaColors.fgDim,
            letterSpacing = 0.1.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun ReadingDateRow(
    recordedAt: String,
    onDateClick: () -> Unit
) {
    OutlinedTextField(
        value = recordedAt,
        onValueChange = {},
        label = { Text("Datum (iz EXIF-a ili ručno)") },
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onDateClick),
        readOnly = true,
        enabled = false,
        colors = OutlinedTextFieldDefaults.colors(
            disabledBorderColor = StrujaColors.borderStrong,
            disabledTextColor = StrujaColors.fgStrong,
            disabledLabelColor = StrujaColors.fgMute
        )
    )
}

private fun createCameraImageUri(context: android.content.Context): Uri {
    val dir = File(context.cacheDir, "images").apply { mkdirs() }
    val file = File.createTempFile("meter_", ".jpg", dir)
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )
}

@Composable
private fun ReadingForm(
    vt: String,
    mt: String,
    recordedAt: String,
    onVtChange: (String) -> Unit,
    onMtChange: (String) -> Unit,
    onDateClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(StrujaColors.surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = vt,
                onValueChange = onVtChange,
                label = { Text("VT") },
                modifier = Modifier.weight(1f),
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
            OutlinedTextField(
                value = mt,
                onValueChange = onMtChange,
                label = { Text("MT") },
                modifier = Modifier.weight(1f),
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

        OutlinedTextField(
            value = recordedAt,
            onValueChange = {},
            label = { Text("Datum") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onDateClick),
            readOnly = true,
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = StrujaColors.borderStrong,
                disabledTextColor = StrujaColors.fgStrong,
                disabledLabelColor = StrujaColors.fgMute
            )
        )
    }
}
