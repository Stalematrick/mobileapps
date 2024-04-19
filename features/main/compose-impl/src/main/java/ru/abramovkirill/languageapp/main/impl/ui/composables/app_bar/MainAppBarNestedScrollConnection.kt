package ru.abramovkirill.languageapp.main.impl.ui.composables.app_bar

import androidx.annotation.FloatRange
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
abstract class MainAppBarNestedScrollConnection(
    density: Density,
    protected val appBarMaxHeight: Dp
) : NestedScrollConnection {
    val appBarMaxHeightPx = with(density) { appBarMaxHeight.toPx() }

    @Composable
    @FloatRange(from = 0.0, to = 1.0)
    abstract fun getAppBarProgressState(): State<Float>
}

class MainAppBarDefaultNestedScrollConnection(
    density: Density,
) : MainAppBarNestedScrollConnection(
    density = density,
    appBarMaxHeight = 0.dp
) {
    @Composable
    override fun getAppBarProgressState(): State<Float> {
        return remember { mutableFloatStateOf(1f) }
    }
}


class MainAppBarSnapNestedScrollConnection(
    density: Density,
    appBarMaxHeight: Dp,
    private val scrollableState: ScrollableState,
) : MainAppBarNestedScrollConnection(
    density = density,
    appBarMaxHeight = appBarMaxHeight,
) {
    private var scrollOffset by mutableFloatStateOf(0f)

    @Composable
    @FloatRange(from = 0.0, to = 1.0)
    override fun getAppBarProgressState(): State<Float> {
        val scrollProgress = remember(scrollOffset) {
            (scrollOffset + appBarMaxHeightPx) / appBarMaxHeightPx
        }

        return animateFloatAsState(
            targetValue = if (scrollableState.isScrollInProgress) {
                scrollProgress
            } else {
                if (scrollProgress > 0.5f) 1f else 0f
            },
            label = "DefaultMainAppBarScrollAnimation"
        )
    }

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val delta = available.y.toInt()
        val newOffset = scrollOffset + delta
        val previousOffset = scrollOffset
        scrollOffset = newOffset.coerceIn(-appBarMaxHeightPx, 0f)
        val consumed = scrollOffset - previousOffset
        return Offset(0f, consumed)
    }
}


@Composable
fun rememberAppBarSnapScrollConnection(
    scrollableState: ScrollableState
): MainAppBarNestedScrollConnection {
    val density = LocalDensity.current
    val maxHeight = MainAppBarExpandedHeight

    return remember(density) {
        MainAppBarSnapNestedScrollConnection(
            density = density,
            scrollableState = scrollableState,
            appBarMaxHeight = maxHeight
        )
    }
}
