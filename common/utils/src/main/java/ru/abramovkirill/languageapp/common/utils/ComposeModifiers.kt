package ru.abramovkirill.languageapp.common.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

private val EmptyOnClick: () -> Unit = {}

fun Modifier.consumeClicks() = composed {
    then(
        Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = EmptyOnClick
        )
    )
}
