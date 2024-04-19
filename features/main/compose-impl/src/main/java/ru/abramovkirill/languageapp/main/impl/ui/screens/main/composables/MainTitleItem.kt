package ru.abramovkirill.languageapp.main.impl.ui.screens.main.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.abramovkirill.languageapp.core.design.composables.shimmerEffect
import ru.abramovkirill.languageapp.core.design.styles.AppTheme

@Composable
fun MainTitleItem(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        style = AppTheme.typography.titleMedium,
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium,
        color = AppTheme.colors.onBackground,
        modifier = modifier,
    )
}

@Composable
fun MainTitleShimmerItem(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .requiredSize(width = 100.dp, height = 20.dp)
                .clip(AppTheme.shapes.medium)
                .shimmerEffect()
        )
    }
}
