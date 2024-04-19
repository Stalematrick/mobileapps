package ru.abramovkirill.languageapp.common.utils

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource

@Immutable
sealed interface VectorResource {
    data class ByRes(@DrawableRes val res: Int): VectorResource
}

fun vectorRes(@DrawableRes res: Int) =
    VectorResource.ByRes(res)

@Composable
fun VectorResource.extract() = when (this) {
    is VectorResource.ByRes -> ImageVector.vectorResource(res)
}

fun VectorResource.testTag() = when (this) {
    is VectorResource.ByRes -> res.toString()
}
