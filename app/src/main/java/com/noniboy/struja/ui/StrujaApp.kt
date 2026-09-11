package com.noniboy.struja.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.noniboy.struja.navigation.StrujaNavGraph
import com.noniboy.struja.ui.components.SettingsDialog
import com.noniboy.struja.ui.components.StrujaHeader
import com.noniboy.struja.ui.theme.StrujaColors

@Composable
fun StrujaApp(
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val settingsState by settingsViewModel.uiState.collectAsState()

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        if (uri != null) settingsViewModel.onExportFilePicked(uri)
        else settingsViewModel.onExportCancelled()
    }

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let { settingsViewModel.onImportFilePicked(it) }
    }

    settingsState.exportFileName?.let { fileName ->
        LaunchedEffect(fileName) {
            exportLauncher.launch(fileName)
        }
    }

    if (settingsState.showSettingsDialog) {
        SettingsDialog(
            currentApiKey = settingsState.apiKey,
            onSave = { key ->
                settingsViewModel.updateApiKey(key)
                settingsViewModel.saveApiKey()
            },
            onDismiss = { settingsViewModel.hideSettings() },
            backupBusy = settingsState.backupBusy,
            backupMessage = settingsState.backupMessage,
            pendingImport = settingsState.pendingImport,
            onExportClick = { settingsViewModel.onExportClick() },
            onImportClick = { importLauncher.launch(arrayOf("application/json")) },
            onConfirmImport = { settingsViewModel.confirmImport() },
            onCancelImport = { settingsViewModel.cancelImport() },
            onDismissBackupMessage = { settingsViewModel.dismissBackupMessage() }
        )
    }

    Scaffold(
        topBar = {
            StrujaHeader(
                onSettingsClick = { settingsViewModel.showSettings() }
            )
        },
        containerColor = StrujaColors.bg,
        contentColor = StrujaColors.fg
    ) { paddingValues ->
        StrujaNavGraph(
            navController = navController,
            modifier = Modifier.padding(paddingValues)
        )
    }
}
