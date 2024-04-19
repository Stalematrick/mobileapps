package ru.abramovkirill.languageapp.core.design.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.abramovkirill.languageapp.core.design.styles.AppTheme

@Composable
fun DotsProgressIndicator(
    progress: Int,
    total: Int,
    modifier: Modifier = Modifier,
    indicatorRadius: Dp = 4.dp,
    indicatorsSpacing: Dp = 8.dp,
    indicatorColor: Color = AppTheme.colors.progressBackground,
    progressIndicatorColor: Color = AppTheme.colors.primaryContainer,
) {
    val currentDensity = LocalDensity.current

    val indicatorRadiusPx = remember(indicatorRadius, currentDensity) {
        with(currentDensity) { indicatorRadius.toPx() }
    }

    val indicatorsSpacingPx = remember(indicatorsSpacing, currentDensity) {
        with(currentDensity) { indicatorsSpacing.toPx() }
    }

    val contentWidth = remember(indicatorRadius, indicatorsSpacing, total) {
        indicatorRadius * 2 * total + indicatorsSpacing * (total - 1)
    }

    val contentHeight = remember(indicatorRadius) {
        indicatorRadius * 2
    }

    val progressDotStartPosition by animateFloatAsState(
        targetValue = progress * indicatorRadiusPx * 2 + progress * indicatorsSpacingPx,
        animationSpec = tween(
            durationMillis = DOT_ANIMATION_DURATION_MILLIS,
            delayMillis = DOT_ANIMATION_DURATION_MILLIS,
        ),
        label = "DotsProgressIndicator:ProgressDot",
    )

    val progressDotEndPosition by animateFloatAsState(
        targetValue = progress * indicatorRadiusPx * 2 + progress * indicatorsSpacingPx + indicatorRadiusPx * 2,
        animationSpec = tween(
            durationMillis = DOT_ANIMATION_DURATION_MILLIS,
        ),
        label = "DotsProgressIndicator:ProgressDot",
    )

    IndicatorsContainer(
        contentWidth = contentWidth,
        contentHeight = contentHeight,
        modifier = modifier,
        content = {
            drawIndicators(
                total = total,
                indicatorColor = indicatorColor,
                indicatorRadiusPx = indicatorRadiusPx,
                indicatorsSpacingPx = indicatorsSpacingPx,
            )

            drawProgressIndicator(
                progressIndicatorColor = progressIndicatorColor,
                progressDotStartPosition = progressDotStartPosition,
                progressDotEndPosition = progressDotEndPosition,
                indicatorRadiusPx = indicatorRadiusPx,
            )
        }
    )
}

@Composable
private fun IndicatorsContainer(
    contentWidth: Dp,
    contentHeight: Dp,
    modifier: Modifier = Modifier,
    content: DrawScope.() -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Canvas(
            onDraw = content,
            modifier = Modifier
                .size(width = contentWidth, height = contentHeight),
        )
    }
}

private fun DrawScope.drawIndicators(
    total: Int,
    indicatorColor: Color,
    indicatorRadiusPx: Float,
    indicatorsSpacingPx: Float,
) {
    repeat(total) { index ->
        drawCircle(
            color = indicatorColor,
            center = Offset(
                x = index * indicatorRadiusPx * 2 + index * indicatorsSpacingPx + indicatorRadiusPx,
                y = indicatorRadiusPx,
            )
        )
    }
}

private fun DrawScope.drawProgressIndicator(
    progressIndicatorColor: Color,
    progressDotStartPosition: Float,
    progressDotEndPosition: Float,
    indicatorRadiusPx: Float,
) {
    drawRoundRect(
        color = progressIndicatorColor,
        topLeft = Offset(
            x = progressDotStartPosition,
            y = 0f
        ),
        size = Size(
            width = progressDotEndPosition - progressDotStartPosition,
            height = indicatorRadiusPx * 2
        ),
        cornerRadius = CornerRadius(
            x = indicatorRadiusPx,
            y = indicatorRadiusPx,
        )
    )
}

private const val DOT_ANIMATION_DURATION_MILLIS = 200

@Composable
@Preview(
    showBackground = true,
)
private fun DotsProgressIndicatorPreview() {
    AppTheme {
        DotsProgressIndicator(
            progress = 0,
            total = 3,
            modifier = Modifier.wrapContentSize()
        )
    }
}
