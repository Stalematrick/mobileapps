package ru.abramovkirill.languageapp.onboarding.impl.ui.screens.onboarding

import android.content.res.Configuration
import androidx.activity.SystemBarStyle
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.abramovkirill.languageapp.common.utils.StringResource
import ru.abramovkirill.languageapp.common.utils.VectorResource
import ru.abramovkirill.languageapp.common.utils.extract
import ru.abramovkirill.languageapp.common.utils.strRes
import ru.abramovkirill.languageapp.common.utils.testTag
import ru.abramovkirill.languageapp.common.utils.vectorRes
import ru.abramovkirill.languageapp.core.design.composables.AppRootContainer
import ru.abramovkirill.languageapp.core.design.composables.DotsProgressIndicator
import ru.abramovkirill.languageapp.core.design.composables.animation.FadeInAnimatedContent
import ru.abramovkirill.languageapp.core.design.composables.animation.RightToLeftAnimatedContent
import ru.abramovkirill.languageapp.core.design.composables.button.AppButton
import ru.abramovkirill.languageapp.core.design.composables.button.AppTextButton
import ru.abramovkirill.languageapp.core.design.composables.scaffold.AppScaffoldControllerEffect
import ru.abramovkirill.languageapp.core.design.composables.scaffold.LocalAppSystemBarsStylesDefault
import ru.abramovkirill.languageapp.core.design.composables.scaffold.darkTransparent
import ru.abramovkirill.languageapp.core.design.composables.scaffold.lightTransparent
import ru.abramovkirill.languageapp.core.design.composables.shimmerEffect
import ru.abramovkirill.languageapp.core.design.styles.AppTheme
import ru.abramovkirill.languageapp.core.design.styles.isAppInDarkTheme
import ru.abramovkirill.languageapp.core.design.utils.COMPOSE_LARGE_DEVICE_SPEC
import ru.abramovkirill.languageapp.core.design.utils.smallDeviceMaxWidth
import ru.abramovkirill.languageapp.onboarding.api.ui.navigation.ONBOARDING_SCREEN_ROUTE_PATH
import ru.abramovkirill.languageapp.onboarding.impl.R
import ru.abramovkirill.languageapp.onboarding.impl.ui.screens.onboarding.OnboardingContract.Intent
import ru.abramovkirill.languageapp.onboarding.impl.ui.screens.onboarding.OnboardingContract.State

@Composable
fun OnboardingScreen(viewModel: OnboardingViewModel) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    OnboardingScreen(
        state = state,
        onIntent = viewModel::processIntent,
    )
}

@Composable
private fun OnboardingScreen(
    state: State,
    onIntent: (Intent) -> Unit,
) {
    AppScaffoldControllerEffect(
        appSystemBarsStyles = LocalAppSystemBarsStylesDefault.current.copy(
            key = ONBOARDING_SCREEN_ROUTE_PATH,
            statusBar = if (isAppInDarkTheme()) {
                SystemBarStyle.darkTransparent()
            } else {
                SystemBarStyle.lightTransparent()
            },
        )
    )

    when (state) {
        is State.Loading -> {
            OnboardingScreenLoading()
        }
        is State.Loaded -> {
            OnboardingScreenLoaded(
                state = state,
                onIntent = onIntent,
            )
        }
    }
}

@Composable
private fun OnboardingScreenLoading() {
    OnboardingScreenLayout(
        imageContent = {
            Box(
                modifier = Modifier
                    .size(220.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect()
            )
        },
        progressContent = {
            Box(
                modifier = Modifier
                    .size(width = 40.dp, height = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect()
            )
        },
        titleContent = {
            Box(
                modifier = Modifier
                    .size(width = 263.dp, height = 28.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect()
            )
        },
        descriptionContent = {
            Box(
                modifier = Modifier
                    .size(width = 263.dp, height = 20.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect()
            )
        },
        primaryButtonContent = { buttonModifier ->
            Box(
                modifier = buttonModifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect()
            )
        },
        secondaryButtonContent = { buttonModifier ->
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = buttonModifier
                    .size(width = 111.dp, height = 20.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .shimmerEffect()
            )
        },
        modifier = Modifier
            .testTag(ONBOARDING_SHIMMER_TEST_TAG)
    )
}

@Composable
private fun OnboardingScreenLoaded(
    state: State.Loaded,
    onIntent: (Intent) -> Unit,
    modifier: Modifier = Modifier,
) {
    OnboardingScreenLayout(
        imageContent = {
            OnboardingImage(vectorResource = state.image)
        },
        progressContent = {
            DotsProgressIndicator(
                progress = state.progress,
                total = state.total
            )
        },
        titleContent = {
            OnboardingTitle(title = state.title)
        },
        descriptionContent = {
            OnboardingDescription(description = state.description)
        },
        primaryButtonContent = { buttonModifier ->
            OnboardingPrimaryButton(
                progress = state.progress,
                total = state.total,
                onClick = { onIntent(Intent.Next) },
                modifier = buttonModifier
                    .fillMaxWidth(),
            )
        },
        secondaryButtonContent = { buttonModifier ->
            AppTextButton(
                text = stringResource(R.string.onboarding_skip),
                onClick = { onIntent(Intent.Skip) },
                modifier = buttonModifier
                    .fillMaxWidth(),
            )
        },
        modifier = modifier
    )
}

@Composable
private fun OnboardingImage(
    vectorResource: VectorResource
) {
    RightToLeftAnimatedContent(
        targetState = vectorResource,
        label = "Onboarding:Image",
        modifier = Modifier
            .fillMaxSize()
    ) { image ->
        Image(
            imageVector = image.extract(),
            contentDescription = null,
            contentScale = ContentScale.Inside,
            modifier = Modifier
                .testTag(image.testTag())
        )
    }
}

@Composable
private fun OnboardingTitle(
    title: StringResource
) {
    FadeInAnimatedContent(
        targetState = title.extract() ?: "",
        label = "Onboarding:Title",
    ) { text ->
        Text(
            text = text,
            style = AppTheme.typography.titleLarge,
            fontWeight = FontWeight.Medium,
            color = AppTheme.colors.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 24.dp)
        )
    }
}

@Composable
private fun OnboardingDescription(
    description: StringResource
) {
    FadeInAnimatedContent(
        targetState = description.extract() ?: "",
        label = "Onboarding:Description",
    ) { text ->
        Text(
            text = text,
            style = AppTheme.typography.bodyMedium,
            fontWeight = FontWeight.Normal,
            color = AppTheme.colors.textBody,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 24.dp)
        )
    }
}

@Composable
private fun OnboardingPrimaryButton(
    progress: Int,
    total: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FadeInAnimatedContent(
        targetState = progress,
        outAfterIn = true,
        label = "Onboarding:Button",
        modifier = modifier,
    ) { buttonStateProgress ->
        AppButton(
            text = when (buttonStateProgress) {
                total - 1 -> stringResource(R.string.onboarding_log_in)
                0 -> stringResource(R.string.onboarding_next)
                else -> stringResource(R.string.onboarding_more)
            },
            onClick = onClick,
        )
    }
}

@Composable
private fun OnboardingScreenLayout(
    imageContent: @Composable BoxScope.() -> Unit,
    progressContent: @Composable ColumnScope.() -> Unit,
    titleContent: @Composable ColumnScope.() -> Unit,
    descriptionContent: @Composable ColumnScope.() -> Unit,
    primaryButtonContent: @Composable ColumnScope.(Modifier) -> Unit,
    secondaryButtonContent: @Composable ColumnScope.(Modifier) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            content = imageContent,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        progressContent()

        Spacer(modifier = Modifier.height(40.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .wrapContentWidth()
                .weight(weight = 0.3f)
        ) {
            titleContent()

            Spacer(modifier = Modifier.height(8.dp))

            descriptionContent()
        }

        primaryButtonContent(
            Modifier
                .smallDeviceMaxWidth()
                .padding(horizontal = 24.dp)
        )

        secondaryButtonContent(
            Modifier
                .smallDeviceMaxWidth()
                .padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(34.dp))
    }
}

class OnboardingScreenPreviewStateProvider : PreviewParameterProvider<State> {
    override val values = sequenceOf(
        State.Loading,
        State.Loaded(
            image = vectorRes(R.drawable.onboarding_conversation_based_learning),
            title = strRes(R.string.onboarding_conversation_based_learning_title),
            description = strRes(R.string.onboarding_conversation_based_learning_description),
            progress = 0,
            total = 3,
        )
    )
}

internal const val ONBOARDING_SHIMMER_TEST_TAG = "Onboarding:Shimmer"

@Composable
private fun OnboardingScreenPreview(
    state: State
) {
    AppRootContainer { _, _ ->
        OnboardingScreen(
            state = state,
            onIntent = {}
        )
    }
}

@Composable
@Preview(
    showBackground = true,
)
private fun OnboardingScreenPreviewLight(
    @PreviewParameter(OnboardingScreenPreviewStateProvider::class) state: State
) {
    OnboardingScreenPreview(state)
}

@Composable
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
private fun OnboardingScreenPreviewDark(
    @PreviewParameter(OnboardingScreenPreviewStateProvider::class) state: State
) {
    OnboardingScreenPreview(state)
}

@Composable
@Preview(
    showBackground = true,
    device = COMPOSE_LARGE_DEVICE_SPEC,
)
private fun OnboardingScreenPreviewLightLarge(
    @PreviewParameter(OnboardingScreenPreviewStateProvider::class) state: State
) {
    OnboardingScreenPreview(state)
}

@Composable
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = COMPOSE_LARGE_DEVICE_SPEC,
)
private fun OnboardingScreenPreviewDarkLarge(
    @PreviewParameter(OnboardingScreenPreviewStateProvider::class) state: State
) {
    OnboardingScreenPreview(state)
}

