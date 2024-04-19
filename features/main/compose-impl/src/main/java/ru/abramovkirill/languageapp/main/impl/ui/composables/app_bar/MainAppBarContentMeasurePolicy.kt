package ru.abramovkirill.languageapp.main.impl.ui.composables.app_bar

import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import kotlin.math.roundToInt

internal class MainAppBarContentMeasurePolicy(
    private val scrollProgress: Float,
    private val statusBarHeight: Dp,
    private val avatarTitleHorizontalMargin: Dp,
    private val avatarTitleVerticalMargin: Dp,
    private val titleDescriptionVerticalMargin: Dp,
) : MeasurePolicy {

    override fun MeasureScope.measure(
        measurables: List<Measurable>,
        constraints: Constraints
    ): MeasureResult {
        val avatarPlaceable = measurables[0].measure(constraints)
        val titlePlaceable = measurables[1].measure(constraints)
        val descriptionPlaceable = measurables[2].measure(constraints)

        val avatarX = 0f
        val avatarY = statusBarHeight.toPx()

        val avatarCenterY = avatarY + (avatarPlaceable.height / 2)

        val titleX = placeablePosition(
            collapsed = avatarPlaceable.width + avatarTitleHorizontalMargin.toPx(),
            expanded = 0f,
            progress = scrollProgress,
        )

        val titleY = placeablePosition(
            collapsed = avatarCenterY - titlePlaceable.height / 2,
            expanded = avatarY + avatarTitleVerticalMargin.toPx() + avatarPlaceable.height,
            progress = scrollProgress,
        )

        val descriptionX = 0f

        val descriptionY = placeablePosition(
            collapsed = 0f,
            expanded = titleY + titleDescriptionVerticalMargin.toPx() + titlePlaceable.height,
            progress = scrollProgress,
        )

        val layoutHeight = arrayOf(
            avatarY + avatarPlaceable.height,
            titleY + titlePlaceable.height,
            descriptionY + descriptionPlaceable.height
        ).max()

        return layout(constraints.maxWidth, layoutHeight.roundToInt()) {
            placeRelative(avatarPlaceable, avatarX, avatarY)
            placeRelative(titlePlaceable, titleX, titleY)
            placeRelative(descriptionPlaceable, descriptionX, descriptionY)
        }
    }

    private fun placeablePosition(
        collapsed: Float,
        expanded: Float,
        progress: Float
    ): Float {
        return collapsed + (expanded - collapsed) * progress
    }


    private fun Placeable.PlacementScope.placeRelative(
        placeable: Placeable,
        x: Float,
        y: Float
    ) {
        placeable.placeRelative(x.roundToInt(), y.roundToInt())
    }
}
