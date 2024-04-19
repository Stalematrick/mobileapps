package ru.abramovkirill.languageapp.core.design.styles

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalInspectionMode

@Composable
fun AppTheme(
    isDarkTheme: Boolean = isAppInDarkTheme(),
    content: @Composable () -> Unit
) {
    val appColors = if (isDarkTheme) {
        darkAppColors
    } else {
        lightAppColors
    }

    CompositionLocalProvider(LocalAppColors provides appColors) {
        MaterialTheme(
            colorScheme = appColors.materialColors,
            shapes = appShapes,
            typography = appTypography,
            content = {
                content()
            }
        )
    }
}

val LocalAppUiMode = staticCompositionLocalOf<AppUiMode> {
    throw IllegalStateException("No app ui mode provider")
}

@Composable
fun isAppInDarkTheme(): Boolean {
    return if (LocalInspectionMode.current) {
        isSystemInDarkTheme()
    } else {
        val appUiMode = LocalAppUiMode.current
        appUiMode == AppUiMode.DARK
    }
}

object AppTheme {
    val colors: AppColors
        @Composable
        get() = LocalAppColors.current

    val shapes: Shapes
        @Composable
        get() = MaterialTheme.shapes

    val typography: Typography
        @Composable
        get() = MaterialTheme.typography
}
