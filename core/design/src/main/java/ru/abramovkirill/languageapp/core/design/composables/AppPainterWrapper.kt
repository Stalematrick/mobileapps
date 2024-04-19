package ru.abramovkirill.languageapp.core.design.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.painter.Painter
import ru.abramovkirill.languageapp.common.utils.ImagePainterState
import ru.abramovkirill.languageapp.common.utils.PainterResource
import ru.abramovkirill.languageapp.common.utils.ImagePainterSize
import ru.abramovkirill.languageapp.common.utils.extract

@Composable
fun AppPainterWrapper(
    painterResource: PainterResource,
    size: ImagePainterSize = ImagePainterSize.Original,
    successContent: @Composable (Painter) -> Unit,
    errorContent: @Composable () -> Unit = {},
    loadingContent: @Composable () -> Unit = {},
) {
    var state by remember { mutableStateOf(ImagePainterState.Loading) }

    val painter = painterResource.extract(
        size = size,
        onState = { state = it }
    )

    when (state) {
        ImagePainterState.Loading -> loadingContent()
        ImagePainterState.Success -> successContent(painter)
        ImagePainterState.Error -> errorContent()
    }
}
