package ru.abramovkirill.languageapp.core.design.composables.button

import androidx.compose.ui.graphics.Color

data class AppButtonColors(
    val idleColor: Color,
    val idleContentColor: Color,
    val loadingColor: Color,
    val loadingContentColor: Color,
    val successColor: Color,
    val successContentColor: Color,
    val errorColor: Color,
    val errorContentColor: Color,
    val disabledColor: Color,
    val disabledContentColor: Color,
)

internal fun AppButtonColors.containerColor(buttonState: AppButtonState) = when (buttonState) {
    AppButtonState.Idle -> idleColor
    AppButtonState.Loading -> loadingColor
    AppButtonState.Success -> successColor
    AppButtonState.Error -> errorColor
    AppButtonState.Disabled -> disabledColor
}

internal fun AppButtonColors.contentColor(buttonState: AppButtonState) = when (buttonState) {
    AppButtonState.Idle -> idleContentColor
    AppButtonState.Loading -> loadingContentColor
    AppButtonState.Success -> successContentColor
    AppButtonState.Error -> errorContentColor
    AppButtonState.Disabled -> disabledContentColor
}
