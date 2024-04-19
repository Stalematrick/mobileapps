package ru.abramovkirill.languageapp.common.utils

import androidx.annotation.Px
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import coil.request.ImageRequest
import coil.size.Dimension

@Immutable
sealed interface PainterResource {
    data class ByPainter(val painter: Painter) : PainterResource
    data class ByData(val data: Any) : PainterResource
}

fun painterRes(data: Any) = PainterResource.ByData(data)

fun painterRes(painter: Painter) = PainterResource.ByPainter(painter)

@Composable
fun PainterResource.extract(
    size: ImagePainterSize = ImagePainterSize.Original,
    onState: (ImagePainterState) -> Unit = {},
): Painter {
    val isInInspectionMode = LocalInspectionMode.current
    
    return if (isInInspectionMode) {
        inspectionModeExtract(size = size, onState = onState)
    } else {
        defaultExtract(size = size, onState = onState)
    }
}

@Composable
private fun PainterResource.inspectionModeExtract(
    size: ImagePainterSize,
    onState: (ImagePainterState) -> Unit
) =
    when {
        this is PainterResource.ByPainter -> painter
        this is PainterResource.ByData && data is Int -> painterResource(id = data)
        else -> defaultExtract(size = size, onState = {})
    }.also { onState(ImagePainterState.Success) }

@Composable
private fun PainterResource.defaultExtract(
    size: ImagePainterSize,
    onState: (ImagePainterState) -> Unit,
) =
    when (this) {
        is PainterResource.ByPainter -> {
            painter.also { onState(ImagePainterState.Success) }
        }
        is PainterResource.ByData -> {
            val context = LocalContext.current
            val imageLoader = context.imageLoader
    
            rememberAsyncImagePainter(
                model = ImageRequest.Builder(context)
                    .data(data)
                    .size(size.toCoilSize())
                    .build(),
                onState = { state ->
                    onState(
                        when (state) {
                            AsyncImagePainter.State.Empty ->
                                ImagePainterState.Loading
                            is AsyncImagePainter.State.Loading ->
                                ImagePainterState.Loading
                            is AsyncImagePainter.State.Error ->
                                ImagePainterState.Error
                            is AsyncImagePainter.State.Success ->
                                ImagePainterState.Success
                        }
                    )
                },
                imageLoader = imageLoader,
            )
        }
    }

data class ImagePainterSize(
    @Px val width: Int,
    @Px val height: Int
) {
    constructor(size: Int): this(size, size)

    companion object {
        const val UNDEFINED = -1
        val Original = ImagePainterSize(UNDEFINED)
    }
}

enum class ImagePainterState {
    Loading,
    Success,
    Error,
}

private fun ImagePainterSize.toCoilSize() = coil.size.Size(
    width = if (width == ImagePainterSize.UNDEFINED) Dimension.Undefined else Dimension.Pixels(width),
    height = if (height == ImagePainterSize.UNDEFINED) Dimension.Undefined else Dimension.Pixels(height),
)
