package com.noniboy.struja.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = StrujaColors.accent,
    onPrimary = StrujaColors.fgStrong,
    primaryContainer = StrujaColors.accent,
    onPrimaryContainer = StrujaColors.fgStrong,
    secondary = StrujaColors.accentStrong,
    onSecondary = StrujaColors.fgStrong,
    secondaryContainer = StrujaColors.surface2,
    onSecondaryContainer = StrujaColors.fg,
    tertiary = StrujaColors.success,
    onTertiary = StrujaColors.fgStrong,
    tertiaryContainer = StrujaColors.surface2,
    onTertiaryContainer = StrujaColors.fg,
    background = StrujaColors.bg,
    onBackground = StrujaColors.fg,
    surface = StrujaColors.surface,
    onSurface = StrujaColors.fg,
    surfaceVariant = StrujaColors.surface2,
    onSurfaceVariant = StrujaColors.fgMute,
    outline = StrujaColors.border,
    outlineVariant = StrujaColors.borderStrong,
    error = StrujaColors.danger,
    onError = StrujaColors.fgStrong,
    errorContainer = StrujaColors.danger,
    onErrorContainer = StrujaColors.fgStrong
)

@Composable
fun StrujaTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as android.app.Activity).window
            window.statusBarColor = StrujaColors.bg.toArgb()
            window.navigationBarColor = StrujaColors.bg.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = StrujaTypography,
        content = content
    )
}
