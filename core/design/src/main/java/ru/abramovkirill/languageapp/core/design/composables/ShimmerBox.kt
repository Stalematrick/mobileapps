package ru.abramovkirill.languageapp.core.design.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ru.abramovkirill.languageapp.core.design.styles.AppTheme

@Composable
fun ShimmerBox(
    modifier: Modifier = Modifier,
    shimmerBackgroundColor: Color = AppTheme.colors.shimmerBackground,
    shimmerForegroundColor: Color = AppTheme.colors.shimmerForeground,
) {
    Box(
        modifier = modifier
            .shimmerEffect(
                shimmerBackgroundColor = shimmerBackgroundColor,
                shimmerForegroundColor = shimmerForegroundColor,
            )
    )
}
