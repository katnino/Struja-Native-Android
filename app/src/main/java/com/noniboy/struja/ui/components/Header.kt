package com.noniboy.struja.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noniboy.struja.ui.theme.GreatVibesFontFamily
import com.noniboy.struja.ui.theme.StrujaColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StrujaHeader(
    onSettingsClick: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "⚡ Struja",
                    fontFamily = GreatVibesFontFamily,
                    fontSize = 24.sp,
                    color = StrujaColors.fgStrong
                )
            }
        },
        actions = {
            Text(
                text = "⚙",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clickable(onClick = onSettingsClick)
                    .padding(8.dp),
                fontSize = 20.sp,
                color = StrujaColors.fgMute
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = StrujaColors.surface,
            titleContentColor = StrujaColors.fgStrong,
            actionIconContentColor = StrujaColors.fgMute
        )
    )
}
