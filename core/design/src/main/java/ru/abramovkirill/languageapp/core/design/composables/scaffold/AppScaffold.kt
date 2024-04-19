package ru.abramovkirill.languageapp.core.design.composables.scaffold

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import ru.abramovkirill.languageapp.core.design.composables.AppBar
import ru.abramovkirill.languageapp.core.design.composables.AppBarTitleGravity
import ru.abramovkirill.languageapp.core.design.composables.AppSnackbarHost
import ru.abramovkirill.languageapp.core.design.styles.AppTheme

@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    appBarState: AppBarState = AppBarState.Hidden,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    content: @Composable BoxScope.() -> Unit,
) {
    val isInInspectionMode = LocalInspectionMode.current

    Scaffold(
        containerColor = AppTheme.colors.background,
        snackbarHost = {
            AppSnackbarHost(hostState = snackbarHostState)
        },
        topBar = if (isInInspectionMode) {
            // Workaround to properly top bar showing in preview
            appScaffoldTopBarInspectionMode(appBarState)
        } else {
            { AppScaffoldTopBar(appBarState) }
        },
        modifier = modifier
            .fillMaxSize()
    ) { scaffoldPaddingValues ->
        Box(
            content = content,
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(scaffoldPaddingValues)
                .fillMaxSize(),
        )
    }
}

@Composable
private fun AppScaffoldTopBar(
    appBarState: AppBarState,
) {
    var currentTopBarBottom by remember { mutableFloatStateOf(0f) }
    var previousTopBarBottom by remember { mutableFloatStateOf(0f) }
    var enterImmediately by remember { mutableStateOf(false) }
    var exitDelayed by remember { mutableStateOf(false) }

    LaunchedEffect(currentTopBarBottom) {
        if (currentTopBarBottom != previousTopBarBottom) {
            if (currentTopBarBottom < previousTopBarBottom) {
                enterImmediately = true
                exitDelayed = false
            } else {
                enterImmediately = false
                exitDelayed = true
            }

            previousTopBarBottom = currentTopBarBottom
        }
    }

    AppBarAnimatedContent(
        targetState = appBarState,
        enterImmediately = enterImmediately,
        exitDelayed = exitDelayed,
    ) { state ->
        when (state) {
            AppBarState.Hidden -> {
                Box(modifier = Modifier.fillMaxWidth())
            }
            is AppBarState.Custom -> {
                Box(
                    modifier = Modifier
                        .onGloballyPositioned { coordinates ->
                            currentTopBarBottom = coordinates.boundsInParent().bottom
                        }
                ) {
                    state.content()
                }
            }
        }
    }
}

private fun appScaffoldTopBarInspectionMode(
    appBarState: AppBarState,
): @Composable () -> Unit  = {
    when (appBarState) {
        AppBarState.Hidden -> Unit
        is AppBarState.Custom -> {
            appBarState.content()
        }
    }
}

@Composable
private fun AppBarAnimatedContent(
    targetState: AppBarState,
    enterImmediately: Boolean = false,
    exitDelayed: Boolean = false,
    content: @Composable AnimatedContentScope.(targetState: AppBarState) -> Unit,
) {
    AnimatedContent(
        targetState = targetState,
        contentKey = AppBarState::key,
        label = "AppBarStateAnimation",
        transitionSpec = {
            ContentTransform(
                targetContentEnter = slideInVertically(
                    animationSpec = tween(
                        durationMillis = if (enterImmediately) 0 else APP_BAR_ANIMATION_DURATION_MILLIS,
                        easing = LinearEasing,
                    ),
                    initialOffsetY = { fullHeight -> -fullHeight  }
                ),
                initialContentExit = slideOutVertically(
                    animationSpec = tween(
                        durationMillis = APP_BAR_ANIMATION_DURATION_MILLIS,
                        delayMillis = if (exitDelayed) APP_BAR_ANIMATION_DURATION_MILLIS else 0,
                        easing = LinearEasing,
                    ),
                    targetOffsetY = { fullHeight -> -fullHeight }
                )
            )
        },
        content = content,
    )
}

private const val APP_BAR_ANIMATION_DURATION_MILLIS = 200

@Immutable
sealed interface AppBarState {

    val key: String

    data object Hidden : AppBarState {
        override val key: String = this::class.java.simpleName
    }

    data class Custom(
        override val key: String,
        val content: @Composable () -> Unit
    ) : AppBarState
}

@Composable
@Preview(showBackground = true)
private fun AppScaffoldPreview() {
    AppTheme {
        AppScaffold(
            appBarState = AppBarState.Custom(key = "Preview") {
                AppBar(
                    title = "Toolbar Title",
                    titleGravity = AppBarTitleGravity.CENTER,
                    onGoBackClick = {}
                )
            }
        ) {}
    }
}
