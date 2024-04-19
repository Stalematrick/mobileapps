package ru.abramovkirill.languageapp.main.impl.ui.composables.app_bar

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import ru.abramovkirill.languageapp.common.utils.PainterResource
import ru.abramovkirill.languageapp.common.utils.painterRes
import ru.abramovkirill.languageapp.core.design.composables.AppPainterWrapper
import ru.abramovkirill.languageapp.core.design.composables.shimmerEffect
import ru.abramovkirill.languageapp.core.design.styles.AppTheme
import ru.abramovkirill.languageapp.core.design.utils.COMPOSE_LARGE_DEVICE_SPEC
import ru.abramovkirill.languageapp.core.design.utils.isLargeScreen
import ru.abramovkirill.languageapp.main.impl.R
import ru.abramovkirill.languageapp.core.design.R as DesignR

@Composable
fun MainAppBar(
    state: MainAppBarState,
    modifier: Modifier = Modifier,
    onAvatarClick: () -> Unit = {},
    scrollingBehaviour: MainAppBarNestedScrollConnection =
        MainAppBarDefaultNestedScrollConnection(LocalDensity.current),
) {
    when (state) {
        MainAppBarState.Loading ->
            MainAppBarLoading(
                scrollingBehaviour = scrollingBehaviour,
                modifier = modifier,
            )
        is MainAppBarState.Ok ->
            MainAppBarOk(
                state = state,
                onAvatarClick = onAvatarClick,
                scrollingBehaviour = scrollingBehaviour,
                modifier = modifier,
            )
    }
}

@Composable
private fun MainAppBarOk(
    state: MainAppBarState.Ok,
    onAvatarClick: () -> Unit,
    modifier: Modifier = Modifier,
    scrollingBehaviour: MainAppBarNestedScrollConnection,
) {
    MainAppBarLayout(
        avatarContent = {
            AppPainterWrapper(
                painterResource = state.avatar
                    ?: painterRes(DesignR.drawable.ic_avatar_placeholder),
                loadingContent = {
                    Box(
                        modifier = Modifier
                            .requiredSize(AvatarSize)
                            .clip(CircleShape)
                            .shimmerEffect(
                                shimmerBackgroundColor = ShimmerBackgroundColor,
                                shimmerForegroundColor = ShimmerForegroundColor,
                            )
                    )
                },
                successContent = { painter ->
                    Image(
                        painter = painter,
                        contentDescription = stringResource(DesignR.string.accessibility_go_to_profile),
                        modifier = Modifier
                            .size(AvatarSize)
                            .clip(CircleShape)
                            .clickable(onClick = onAvatarClick)
                    )
                }
            )
        },
        titleContent = {
            Text(
                text = if (state.firstName.isNullOrBlank()) {
                    stringResource(R.string.main_app_bar_title)
                } else {
                    stringResource(R.string.main_app_bar_title_named, state.firstName)
                },
                color = AppTheme.colors.onPrimary,
                style = AppTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium
            )
        },
        descriptionContent = {
            Text(
                text = stringResource(R.string.main_app_bar_description),
                color = AppTheme.colors.textBody,
                style = AppTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
        },
        scrollingBehaviour = scrollingBehaviour,
        modifier = modifier,
    )
}

@Composable
private fun MainAppBarLoading(
    modifier: Modifier = Modifier,
    scrollingBehaviour: MainAppBarNestedScrollConnection,
) {
    MainAppBarLayout(
        avatarContent = {
            Box(
                modifier = Modifier
                    .requiredSize(AvatarSize)
                    .clip(CircleShape)
                    .shimmerEffect(
                        shimmerBackgroundColor = ShimmerBackgroundColor,
                        shimmerForegroundColor = ShimmerForegroundColor,
                    )
            )
        },
        titleContent = {
            Box(
                modifier = Modifier
                    .requiredSize(width = 100.dp, height = 22.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .shimmerEffect(
                        shimmerBackgroundColor = ShimmerBackgroundColor,
                        shimmerForegroundColor = ShimmerForegroundColor,
                    )
            )
        },
        descriptionContent = {
            Box(
                modifier = Modifier
                    .requiredSize(width = 200.dp, height = 17.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect(
                        shimmerBackgroundColor = ShimmerBackgroundColor,
                        shimmerForegroundColor = ShimmerForegroundColor,
                    )
            )
        },
        scrollingBehaviour = scrollingBehaviour,
        modifier = modifier,
    )
}

@Composable
private fun MainAppBarLayout(
    scrollingBehaviour: MainAppBarNestedScrollConnection,
    avatarContent: @Composable () -> Unit,
    titleContent: @Composable () -> Unit,
    descriptionContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollProgress by scrollingBehaviour.getAppBarProgressState()

    Box(
        contentAlignment = Alignment.BottomStart,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(AppTheme.colors.primary)
            .padding(
                horizontal = 24.dp,
                vertical = 10.dp,
            )
    ) {
        MainAppBarContent(
            scrollProgress = scrollProgress,
            avatarContent = avatarContent,
            titleContent = titleContent,
            descriptionContent = descriptionContent
        )
    }
}

@Composable
private fun MainAppBarContent(
    scrollProgress: Float,
    avatarContent: @Composable () -> Unit,
    titleContent: @Composable () -> Unit,
    descriptionContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val statusBarHeightPx = WindowInsets.statusBars.getTop(density)

    val descriptionAlpha = (scrollProgress - SCROLL_PROGRESS_BREAK)
        .coerceAtLeast(0f) / (1f - SCROLL_PROGRESS_BREAK)

    val statusBarHeight = remember(statusBarHeightPx, density) {
        with(density) { statusBarHeightPx.toDp() }
    }

    Layout(
        modifier = modifier,
        content = {
            avatarContent()

            titleContent()

            Box(
                modifier = Modifier
                    .alpha(descriptionAlpha)
            ) {
                descriptionContent()
            }
        },
        measurePolicy = MainAppBarContentMeasurePolicy(
            scrollProgress = scrollProgress,
            statusBarHeight = statusBarHeight,
            avatarTitleHorizontalMargin = 16.dp,
            avatarTitleVerticalMargin = 5.dp,
            titleDescriptionVerticalMargin = 5.dp
        )
    )
}

sealed interface MainAppBarState {
    data object Loading : MainAppBarState

    data class Ok(
        val firstName: String?,
        val avatar: PainterResource?
    ) : MainAppBarState
}

private val ShimmerBackgroundColor
    @Composable
    get() = AppTheme.colors.shimmerBackground.copy(alpha = 0.5f)

private val ShimmerForegroundColor
    @Composable
    get() = AppTheme.colors.shimmerForeground

val MainAppBarExpandedHeight
    @Composable
    get() = if (isLargeScreen()) 181.dp else 131.dp

private val AvatarSize
    @Composable
    get() = if (isLargeScreen()) 104.dp else 54.dp

private const val SCROLL_PROGRESS_BREAK = 0.7f

private class MainAppBarPreviewStateProvider : PreviewParameterProvider<MainAppBarState> {
    override val values = sequenceOf(
        MainAppBarState.Loading,
        MainAppBarState.Ok(
            firstName = "Emil",
            avatar = painterRes(DesignR.drawable.ic_avatar_placeholder),
        )
    )
}

@Composable
private fun MainAppBarPreview(
    state: MainAppBarState
) {
    AppTheme {
        MainAppBar(state = state)
    }
}

@Composable
@Preview(showBackground = true)
private fun MainAppBarPreviewLight(
    @PreviewParameter(MainAppBarPreviewStateProvider::class) state: MainAppBarState
) {
    MainAppBarPreview(state = state)
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun MainAppBarPreviewDark(
    @PreviewParameter(MainAppBarPreviewStateProvider::class) state: MainAppBarState
) {
    MainAppBarPreview(state = state)
}

@Composable
@Preview(
    showBackground = true,
    device = COMPOSE_LARGE_DEVICE_SPEC,
)
private fun MainAppBarPreviewLightLarge(
    @PreviewParameter(MainAppBarPreviewStateProvider::class) state: MainAppBarState
) {
    MainAppBarPreview(state = state)
}

@Composable
@Preview(
    showBackground = true,
    device = COMPOSE_LARGE_DEVICE_SPEC,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
private fun MainAppBarPreviewDarkLarge(
    @PreviewParameter(MainAppBarPreviewStateProvider::class) state: MainAppBarState
) {
    MainAppBarPreview(state = state)
}
