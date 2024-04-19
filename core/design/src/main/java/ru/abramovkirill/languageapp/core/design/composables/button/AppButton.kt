package ru.abramovkirill.languageapp.core.design.composables.button

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import ru.abramovkirill.languageapp.common.utils.states.ProcessingState
import ru.abramovkirill.languageapp.core.design.R
import ru.abramovkirill.languageapp.core.design.composables.animation.FadeInAnimatedContent
import ru.abramovkirill.languageapp.core.design.styles.AppTheme

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    lightingEnabled: Boolean = true,
    buttonColors: AppButtonColors = AppButtonDefaults.colors(),
    buttonState: AppButtonState = AppButtonState.Idle,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val currentDensity = LocalDensity.current
    var buttonWidth by remember { mutableFloatStateOf(0f) }
    val buttonShape = AppTheme.shapes.medium
    val buttonBlurColor = AppTheme.colors.onSecondary.copy(BUTTON_BLUR_ALPHA)
    val buttonBlurSize = remember(currentDensity) {
        with(currentDensity) { BUTTON_BLUR_SIZE_DP.dp.toPx() }
    }

    val blurPaint = remember {
        Paint().apply {
            val frameworkPaint = asFrameworkPaint()
            frameworkPaint.maskFilter = BlurMaskFilter(buttonBlurSize, BlurMaskFilter.Blur.NORMAL)
            frameworkPaint.style = android.graphics.Paint.Style.STROKE
            frameworkPaint.color = buttonBlurColor.toArgb()
            frameworkPaint.strokeWidth = buttonBlurSize
        }
    }

    val blurPath = remember { Path() }

    val buttonTopStartRadius = remember(currentDensity) {
        buttonShape.topStart.toPx(Size.Unspecified, currentDensity)
    }

    val buttonTopEndRadius = remember(currentDensity) {
        buttonShape.topEnd.toPx(Size.Unspecified, currentDensity)
    }

    LaunchedEffect(
        blurPath, buttonWidth, buttonBlurSize,
        buttonTopStartRadius, buttonTopEndRadius
    ) {
        calculateBlurPath(
            blurPath = blurPath,
            buttonWidth = buttonWidth,
            buttonBlurSize = buttonBlurSize,
            buttonTopStartRadius = buttonTopStartRadius,
            buttonTopEndRadius = buttonTopEndRadius,
        )
    }

    FadeInAnimatedContent(
        targetState = buttonState,
        label = "AppButtonAnimation",
        modifier = modifier
    ) { state ->
        AppButton(
            text = text,
            buttonState = state,
            onClick = onClick,
            buttonShape = buttonShape,
            buttonColors = buttonColors,
            blurEnabled = lightingEnabled,
            blurPath = blurPath,
            blurPaint = blurPaint,
            interactionSource = interactionSource,
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    buttonWidth = coordinates.size.width.toFloat()
                }
        )
    }
}

private fun calculateBlurPath(
    blurPath: Path,
    buttonWidth: Float,
    buttonBlurSize: Float,
    buttonTopStartRadius: Float,
    buttonTopEndRadius: Float,
) {
    with(blurPath) {
        reset()

        moveTo(x = 0f, y = buttonTopStartRadius)

        cubicTo(
            x1 = buttonTopStartRadius * 0.1f, y1 = buttonBlurSize + buttonTopStartRadius * 0.3f,
            x2 = buttonTopStartRadius * 0.3f, y2 = buttonBlurSize + buttonTopStartRadius * 0.1f,
            x3 = buttonTopStartRadius, y3 = buttonBlurSize,
        )

        lineTo(x = buttonWidth - buttonTopEndRadius, y = buttonBlurSize)

        cubicTo(
            x1 = buttonWidth - buttonTopEndRadius * 0.3f, y1 = buttonBlurSize + buttonTopEndRadius * 0.1f,
            x2 = buttonWidth - buttonTopEndRadius * 0.1f, y2 = buttonBlurSize + buttonTopEndRadius * 0.3f,
            x3 = buttonWidth, y3 = buttonBlurSize + buttonTopEndRadius,
        )
    }
}

@Composable
private fun AppButton(
    text: String,
    buttonState: AppButtonState,
    buttonShape: Shape,
    buttonColors: AppButtonColors,
    blurPath: Path,
    blurPaint: Paint,
    blurEnabled: Boolean,
    interactionSource: MutableInteractionSource,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val containerColor = buttonColors.containerColor(buttonState)
    val contentColor = buttonColors.contentColor(buttonState)

    Button(
        onClick = onClick,
        shape = buttonShape,
        enabled = buttonState == AppButtonState.Idle,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            disabledContainerColor = containerColor
        ),
        interactionSource = interactionSource,
        contentPadding = PaddingValues(),
        modifier = modifier
            .height(56.dp)
            .clip(buttonShape)
            .drawWithContent {
                drawContent()

                if (blurEnabled && buttonState != AppButtonState.Disabled) {
                    drawIntoCanvas { canvas ->
                        canvas.drawPath(
                            blurPath,
                            blurPaint
                        )
                    }
                }
            }
    ) {
        when (buttonState) {
            AppButtonState.Idle, AppButtonState.Disabled -> {
                Text(
                    text = text,
                    style = AppTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium,
                    color = contentColor,
                )
            }
            AppButtonState.Loading -> {
                CircularProgressIndicator(
                    color = contentColor
                )
            }
            AppButtonState.Success -> {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_round_check_24),
                    tint = contentColor,
                    contentDescription = null
                )
            }
            AppButtonState.Error -> {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_round_cross_24),
                    tint = contentColor,
                    contentDescription = null
                )
            }
        }
    }
}

fun ProcessingState.toButtonState() = when (this) {
    ProcessingState.None -> AppButtonState.Idle
    ProcessingState.InProgress -> AppButtonState.Loading
    ProcessingState.Success -> AppButtonState.Success
    ProcessingState.Error -> AppButtonState.Error
}

private const val BUTTON_BLUR_ALPHA = 0.2f
private const val BUTTON_BLUR_SIZE_DP = 4

class AppButtonStateProvider : PreviewParameterProvider<AppButtonState> {
    override val values = AppButtonState.entries.asSequence()
}

@Composable
@Preview(showBackground = true)
private fun AppButtonPreview(
    @PreviewParameter(AppButtonStateProvider::class) state: AppButtonState,
) {
    AppTheme {
        AppButton(
            text = "Нажми меня",
            buttonState = state,
            onClick = { /* ... */ },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        )
    }
}
