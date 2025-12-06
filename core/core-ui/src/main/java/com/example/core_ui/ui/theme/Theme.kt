package com.example.core_ui.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ----------------------------------------------------------------------------
// LIGHT COLOR SCHEME
// ----------------------------------------------------------------------------

private val LightColorScheme = lightColorScheme(
    primary = TeacherPrimary,
    onPrimary = OnPrimary,
    primaryContainer = StudentPrimary,

    secondary = CommunityPeers,
    secondaryContainer = BlueGradientStart,
    tertiary = Highlight,

    background = Background,
    surface = Surface,
    onSurface = OnSurface,

    error = DangerOverdue,
    onError = OnPrimary,

    outline = ChartLightGray
)


// ----------------------------------------------------------------------------
// DARK COLOR SCHEME
// ----------------------------------------------------------------------------

private val DarkColorScheme = darkColorScheme(
    primary = TeacherPrimary,
    onPrimary = OnPrimary,
    primaryContainer = StudentPrimary,

    secondary = CommunityPeers,
    secondaryContainer = BlueGradientEnd,
    tertiary = Highlight,

    background = Color(0xFF0F172A),
    surface = Color(0xFF1E293B),
    onSurface = Color.White,

    error = DangerOverdue,
    onError = OnPrimary,

    outline = ChartLightGray
)


// ----------------------------------------------------------------------------
// MAIN THEME
// ----------------------------------------------------------------------------

@Composable
fun GurukulTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalView.current.context
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
