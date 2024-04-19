package ru.abramovkirill.languageapp.core.design.composables.scaffold

import android.graphics.Color
import androidx.activity.SystemBarStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.toArgb
import ru.abramovkirill.languageapp.core.design.styles.AppTheme
import ru.abramovkirill.languageapp.core.design.styles.darkAppColors

@Immutable
data class AppSystemBarsStyles(
    val key: String,
    val statusBar: SystemBarStyle,
    val navigationBar: SystemBarStyle,
) {
    companion object {
        @Composable
        fun default(isDarkTheme: Boolean): AppSystemBarsStyles {
            return AppSystemBarsStyles(
                key = "default",
                statusBar = SystemBarStyle.darkTransparent(),
                navigationBar = if (isDarkTheme) {
                    SystemBarStyle.dark(scrim = AppTheme.colors.background.toArgb())
                } else {
                    SystemBarStyle.light(
                        scrim = AppTheme.colors.background.toArgb(),
                        darkScrim = darkAppColors.background.toArgb(),
                    )
                },
            )
        }
    }
}

val LocalAppSystemBarsStylesDefault = staticCompositionLocalOf<AppSystemBarsStyles> {
    throw IllegalStateException("No AppSystemBarsStyles provided")
}

fun SystemBarStyle.Companion.darkTransparent() =
    dark(scrim = Color.TRANSPARENT)

fun SystemBarStyle.Companion.lightTransparent() =
    light(scrim = Color.TRANSPARENT, darkScrim = Color.TRANSPARENT)
