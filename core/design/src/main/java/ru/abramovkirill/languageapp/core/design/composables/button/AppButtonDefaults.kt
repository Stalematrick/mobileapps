package ru.abramovkirill.languageapp.core.design.composables.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import ru.abramovkirill.languageapp.core.design.styles.AppTheme

object AppButtonDefaults {
    @Composable
    fun colors(
        idleColor: Color = AppTheme.colors.secondary,
        idleContentColor: Color = AppTheme.colors.onSecondary,
        loadingColor: Color = AppTheme.colors.progressBackground,
        loadingContentColor: Color = AppTheme.colors.primaryContainer,
        successColor: Color = AppTheme.colors.success,
        successContentColor: Color = AppTheme.colors.onSuccess,
        errorColor: Color = AppTheme.colors.error,
        errorContentColor: Color = AppTheme.colors.onError,
        disabledColor: Color = AppTheme.colors.surface,
        disabledContentColor: Color = AppTheme.colors.onSecondary,
    ) = AppButtonColors(
        idleColor = idleColor,
        idleContentColor = idleContentColor,
        loadingColor = loadingColor,
        loadingContentColor = loadingContentColor,
        successColor = successColor,
        successContentColor = successContentColor,
        errorColor = errorColor,
        errorContentColor = errorContentColor,
        disabledColor = disabledColor,
        disabledContentColor = disabledContentColor,
    )
}
