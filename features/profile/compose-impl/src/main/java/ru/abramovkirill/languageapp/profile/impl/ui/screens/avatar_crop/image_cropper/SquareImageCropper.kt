@file:OptIn(FlowPreview::class)

package ru.abramovkirill.languageapp.profile.impl.ui.screens.avatar_crop.image_cropper

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.graphics.AndroidPaint
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.abramovkirill.languageapp.common.utils.aspectRatio
import ru.abramovkirill.languageapp.common.utils.coerceIn
import ru.abramovkirill.languageapp.common.utils.plus
import ru.abramovkirill.languageapp.common.utils.roundToIntOffset
import ru.abramovkirill.languageapp.common.utils.roundToIntSize
import ru.abramovkirill.languageapp.core.design.styles.AppTheme
import kotlin.math.min
import ru.abramovkirill.languageapp.core.design.R as DesignR

private const val CROP_CHANGING_FLOW_DEBOUNCE = 100L
private const val MIN_SCALE = 1f

@Composable
@Suppress("LongMethod")
fun SquareImageCropper(
    painter: Painter,
    modifier: Modifier = Modifier,
    maxScale: Float = 5f,
    croppingBoxPadding: Dp = 0.dp,
    croppingBoxStrokeColor: Color = AppTheme.colors.onImageCropperBackground,
    croppingBoxStrokeWidth: Dp = 1.dp,
    croppingBoxCornerWidth: Dp = 20.dp,
    backgroundColor: Color = AppTheme.colors.imageCropperTransparentBackground,
    onCropChanged: (offset: IntOffset, size: IntSize) -> Unit,
) {
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }

    val density = LocalDensity.current

    val croppingBoxStrokeWidthPx = remember(density, croppingBoxStrokeWidth) {
        with(density) { croppingBoxStrokeWidth.toPx() }
    }

    val croppingBoxCornerWidthPx = remember(density, croppingBoxCornerWidth) {
        with(density) { croppingBoxCornerWidth.toPx() }
    }

    val croppingBoxPaddingPx = remember(density, croppingBoxPadding) {
        with(density) { croppingBoxPadding.toPx() }
    }

    val croppingBoxSize by remember {
        derivedStateOf {
            val minBoxDimension = min(canvasSize.width, canvasSize.height)

            val height = minBoxDimension - croppingBoxPaddingPx * 2
            val width = height

            Size(width.coerceAtLeast(0f), height.coerceAtLeast(0f))
        }
    }

    val croppingBoxOffset by remember {
        derivedStateOf {
            val offsetX = (canvasSize.width - croppingBoxSize.width) / 2
            val offsetY = (canvasSize.height - croppingBoxSize.height) / 2
            Offset(offsetX, offsetY)
        }
    }

    val croppingBoxPath = remember { Path() }

    LaunchedEffect(croppingBoxOffset, croppingBoxSize) {
        val left = croppingBoxOffset.x
        val top = croppingBoxOffset.y
        val right = left + croppingBoxSize.width
        val bottom = top + croppingBoxSize.height

        with(croppingBoxPath) {
            reset()

            moveTo(left, top + croppingBoxCornerWidthPx)
            lineTo(left, top)
            lineTo(left + croppingBoxCornerWidthPx, top)

            moveTo(right - croppingBoxCornerWidthPx, top)
            lineTo(right, top)
            lineTo(right, top + croppingBoxCornerWidthPx)

            moveTo(left, bottom - croppingBoxCornerWidthPx)
            lineTo(left, bottom)
            lineTo(left + croppingBoxCornerWidthPx, bottom)

            moveTo(right - croppingBoxCornerWidthPx, bottom)
            lineTo(right, bottom)
            lineTo(right, bottom - croppingBoxCornerWidthPx)
        }
    }

    val imageInitialSize by remember {
        derivedStateOf {
            val width: Float
            val height: Float

            if (painter.intrinsicSize.isSpecified) {
                val aspectRatio = painter.intrinsicSize.aspectRatio

                if (painter.intrinsicSize.width > painter.intrinsicSize.height) {
                    height = croppingBoxSize.height
                    width = height * aspectRatio
                } else {
                    width = croppingBoxSize.width
                    height = width / aspectRatio
                }
            } else {
                width = 0f
                height = 0f
            }

            Size(width.coerceAtLeast(0f), height.coerceAtLeast(0f))
        }
    }

    var scale by remember { mutableFloatStateOf(1f) }

    val imageSize by remember {
        derivedStateOf {
            imageInitialSize * scale
        }
    }

    var croppingBoxTranslation by remember {
        mutableStateOf(
            Offset(
                x = 0f,
                y = 0f,
            )
        )
    }

    val croppingBoxTranslationBounds by remember {
        derivedStateOf {
            Rect(
                left = 0f,
                top = 0f,
                right = imageSize.width - croppingBoxSize.width,
                bottom = imageSize.height - croppingBoxSize.height,
            )
        }
    }

    LaunchedEffect(painter.intrinsicSize) {
        snapshotFlow {
            when {
                painter.intrinsicSize.isSpecified -> {
                    val painterSize = painter.intrinsicSize

                    val offset = Offset(
                        x = croppingBoxTranslation.x / imageSize.width * painterSize.width,
                        y = croppingBoxTranslation.y / imageSize.height * painterSize.height,
                    )

                    val size = Size(
                        width = croppingBoxSize.width / imageSize.width * painterSize.width,
                        height = croppingBoxSize.height / imageSize.height * painterSize.height,
                    )

                    ImageCroppedBounds.Calculated(offset, size)
                }

                else -> ImageCroppedBounds.NotCalculated
            }
        }
            .debounce(CROP_CHANGING_FLOW_DEBOUNCE)
            .filterIsInstance<ImageCroppedBounds.Calculated>()
            .onEach { (offset, size) ->
                onCropChanged(
                    offset.roundToIntOffset(),
                    size.roundToIntSize(),
                )
            }
            .launchIn(this)
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .clip(RectangleShape)
            .onGloballyPositioned { coordinates ->
                canvasSize = coordinates.size
            }
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    if (imageSize == Size.Zero) {
                        return@detectTransformGestures
                    }

                    val oldScale = scale

                    scale = (scale * zoom).coerceIn(
                        minimumValue = MIN_SCALE,
                        maximumValue = maxScale
                    )

                    val scaleOffset =
                        (croppingBoxTranslation + croppingBoxSize / 2f) * (scale / oldScale - 1)

                    croppingBoxTranslation = (croppingBoxTranslation - pan + scaleOffset)
                        .coerceIn(croppingBoxTranslationBounds)
                }
            }
    ) {
        with(painter) {
            translate(
                left = -croppingBoxTranslation.x + croppingBoxOffset.x,
                top = -croppingBoxTranslation.y + croppingBoxOffset.y,
            ) {
                draw(
                    size = imageSize
                )
            }
        }

        drawCroppingCircle(
            backgroundColor = backgroundColor,
            croppingBoxOffset = croppingBoxOffset,
            croppingBoxSize = croppingBoxSize,
        )

        drawCroppingBox(
            croppingBoxStrokeColor = croppingBoxStrokeColor,
            croppingBoxStrokeWidthPx = croppingBoxStrokeWidthPx,
            croppingBoxPath = croppingBoxPath,
        )
    }
}

private fun DrawScope.drawCroppingCircle(
    backgroundColor: Color,
    croppingBoxOffset: Offset,
    croppingBoxSize: Size,
) {
    drawIntoCanvas { canvas ->
        canvas.saveLayer(Rect(Offset.Zero, size), AndroidPaint())

        drawRect(
            color = backgroundColor,
            size = size,
        )

        drawCircle(
            color = Color.Transparent,
            radius = croppingBoxSize.width / 2,
            center = Offset(
                x = croppingBoxOffset.x + croppingBoxSize.width / 2,
                y = croppingBoxOffset.y + croppingBoxSize.height / 2,
            ),
            blendMode = BlendMode.SrcOut
        )

        canvas.restore()
    }
}

private fun DrawScope.drawCroppingBox(
    croppingBoxStrokeColor: Color,
    croppingBoxStrokeWidthPx: Float,
    croppingBoxPath: Path,
) {
    drawPath(
        path = croppingBoxPath,
        color = croppingBoxStrokeColor,
        style = Stroke(
            width = croppingBoxStrokeWidthPx,
        )
    )
}

@Composable
@Preview
private fun RectangleImageCropperPreview() {
    AppTheme {
        SquareImageCropper(
            painter = painterResource(DesignR.drawable.ic_avatar_placeholder),
            onCropChanged = { _, _ ->  },
            modifier = Modifier
                .fillMaxSize()
        )
    }
}
