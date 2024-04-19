package ru.abramovkirill.languageapp.core.design.composables.animation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun <S> RightToLeftAnimatedContent(
    targetState: S,
    modifier: Modifier = Modifier,
    label: String = "AnimatedContent",
    contentKey: (targetState: S) -> Any? = { it },
    content: @Composable AnimatedContentScope.(targetState: S) -> Unit,
) {
    AnimatedContent(
        targetState = targetState,
        contentKey = contentKey,
        label = label,
        transitionSpec = {
            slideInHorizontally(initialOffsetX = { width -> width }) togetherWith
                    slideOutHorizontally(
                        animationSpec = tween(RIGHT_TO_LEFT_ANIMATION_DURATION_MILLIS),
                        targetOffsetX = { width -> -width }
                    )
        },
        content = content,
        modifier = modifier,
    )
}

private const val RIGHT_TO_LEFT_ANIMATION_DURATION_MILLIS = 350
