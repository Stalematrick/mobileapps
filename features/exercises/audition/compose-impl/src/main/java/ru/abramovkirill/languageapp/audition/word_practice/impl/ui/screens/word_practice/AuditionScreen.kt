@file:OptIn(ExperimentalPermissionsApi::class)

package ru.abramovkirill.languageapp.audition.word_practice.impl.ui.screens.word_practice

import android.Manifest
import android.content.res.Configuration
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.flow.Flow
import ru.abramovkirill.languageapp.audition.word_practice.impl.ui.screens.word_practice.AuditionContract.Intent
import ru.abramovkirill.languageapp.audition.word_practice.impl.ui.screens.word_practice.AuditionContract.SideEffect
import ru.abramovkirill.languageapp.audition.word_practice.impl.ui.screens.word_practice.AuditionContract.State
import ru.abramovkirill.languageapp.common.utils.compose.ScreenSideEffectsFlowHandler
import ru.abramovkirill.languageapp.common.utils.compose.handleMessageSideEffect
import ru.abramovkirill.languageapp.core.design.composables.AppBar
import ru.abramovkirill.languageapp.core.design.composables.AppRootContainer
import ru.abramovkirill.languageapp.core.design.composables.ShimmerBox
import ru.abramovkirill.languageapp.core.design.composables.button.AppButton
import ru.abramovkirill.languageapp.core.design.composables.button.AppButtonDefaults
import ru.abramovkirill.languageapp.core.design.composables.button.AppButtonState
import ru.abramovkirill.languageapp.core.design.composables.scaffold.AppBarState
import ru.abramovkirill.languageapp.core.design.composables.scaffold.AppScaffoldControllerEffect
import ru.abramovkirill.languageapp.core.design.composables.scaffold.LocalAppScaffoldController
import ru.abramovkirill.languageapp.core.design.composables.text_field.AppTextField
import ru.abramovkirill.languageapp.core.design.styles.AppTheme
import ru.abramovkirill.languageapp.core.design.utils.COMPOSE_LARGE_DEVICE_SPEC
import ru.abramovkirill.languageapp.core.design.utils.smallDeviceMaxWidth
import ru.abramovkirill.languageapp.exercises.audition.api.ui.navigation.AUDITION_SCREEN_ROUTE_PATH
import ru.abramovkirill.languageapp.exercises.audition.impl.R
import ru.abramovkirill.languageapp.core.design.R as DesignR

@Composable
internal fun WordPracticeScreen(viewModel: AuditionViewModel) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val sideEffects = viewModel.container.sideEffectFlow

    ScreenSideEffects(
        sideEffects = sideEffects,
        onIntent = viewModel::processIntent,
    )

    WordPracticeScreen(
        state = state,
        onIntent = viewModel::processIntent
    )
}

@Composable
private fun WordPracticeScreen(
    state: State,
    onIntent: (Intent) -> Unit,
) {
    AppScaffoldControllerEffect(
        appBarState = AppBarState.Custom(key = AUDITION_SCREEN_ROUTE_PATH) {
            AppBar(
                title = stringResource(R.string.audition_title),
                onGoBackClick = { onIntent(Intent.OnGoBackClick) }
            )
        }
    )

    when (state) {
        State.Loading ->
            ScreenShimmer()
        is State.Resolving ->
            ScreenResolving(
                state = state,
                onIntent = onIntent
            )
        State.Error ->
            ScreenError(
                onIntent = onIntent
            )
    }
}

@Composable
private fun ScreenResolving(
    state: State.Resolving,
    onIntent: (Intent) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = state.word,
            textAlign = TextAlign.Center,
            style = AppTheme.typography.headlineMedium,
            fontWeight = FontWeight.Medium,
            color = AppTheme.colors.onBackground,
        )

        if (state.wordTranscription != null) {
            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = state.wordTranscription,
                textAlign = TextAlign.Center,
                style = AppTheme.typography.titleMedium,
                fontWeight = FontWeight.Normal,
                color = AppTheme.colors.onBackground,
            )
        }

        Spacer(modifier = Modifier.height(35.dp))

        Text(
            text = stringResource(R.string.audition_description),
            style = AppTheme.typography.titleLarge,
            fontWeight = FontWeight.Medium,
            color = AppTheme.colors.onBackground,
            modifier = Modifier
                .smallDeviceMaxWidth(),
        )

        Spacer(modifier = Modifier.height(8.dp))

        AppTextField(
            value = state.result,
            placeholder = stringResource(R.string.audition_start_speaking),
            label = stringResource(R.string.audition_your_result),
            modifier = Modifier
                .smallDeviceMaxWidth(),
        )

        Spacer(modifier = Modifier.weight(1f))

        ResolvingButton(
            step = state.step,
            onStartListeningRequest = { onIntent(Intent.OnStartListening) },
            onStopListeningRequest = { onIntent(Intent.OnStopListening) },
            onNextClick = { onIntent(Intent.OnNextButtonClick) },
            onTryAgainClick = { onIntent(Intent.OnTryAgainButtonClick) },
            modifier = Modifier
                .smallDeviceMaxWidth()
                .fillMaxWidth()
        )
    }
}

@Composable
private fun ResolvingButton(
    step: State.Resolving.Step,
    onStartListeningRequest: () -> Unit,
    onStopListeningRequest: () -> Unit,
    onNextClick: () -> Unit,
    onTryAgainClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    LaunchedEffect(step, isPressed) {
        if (step == State.Resolving.Step.None && isPressed) {
            onStartListeningRequest()
        } else if (step == State.Resolving.Step.Listening && !isPressed) {
            onStopListeningRequest()
        }
    }

    val buttonScale by step.buttonScale()

    AppButton(
        text = step.buttonText(),
        buttonColors = step.buttonColors(),
        interactionSource = interactionSource,
        buttonState = step.buttonState(),
        onClick = {
            when (step) {
                State.Resolving.Step.Success -> onNextClick()
                State.Resolving.Step.Error -> onTryAgainClick()
                else -> Unit
            }
        },
        modifier = modifier.scale(buttonScale)
    )
}

@Composable
private fun ScreenShimmer() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        ShimmerBox(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(width = 100.dp, height = 28.dp)
                .clip(AppTheme.shapes.small)
        )

        Spacer(modifier = Modifier.height(2.dp))

        ShimmerBox(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(width = 85.dp, height = 17.dp)
                .clip(AppTheme.shapes.small)
        )

        Spacer(modifier = Modifier.height(35.dp))

        Column(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .smallDeviceMaxWidth()
        ) {
            ShimmerBox(
                modifier = Modifier
                    .size(width = 200.dp, height = 20.dp)
                    .clip(AppTheme.shapes.small)
            )

            Spacer(modifier = Modifier.height(5.dp))

            ShimmerBox(
                modifier = Modifier
                    .size(width = 85.dp, height = 20.dp)
                    .clip(AppTheme.shapes.small)
            )

            Spacer(modifier = Modifier.height(8.dp))

            ShimmerBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(AppTheme.shapes.small)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        ShimmerBox(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .smallDeviceMaxWidth()
                .fillMaxWidth()
                .height(56.dp)
                .clip(AppTheme.shapes.medium)
        )
    }
}

@Composable
private fun ScreenError(
    onIntent: (Intent) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Image(
            painter = painterResource(DesignR.drawable.image_failed),
            contentScale = ContentScale.FillWidth,
            contentDescription = null,
            modifier = Modifier
                .width(160.dp)
                .clip(AppTheme.shapes.medium),
        )

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = stringResource(DesignR.string.error_smth_went_wrong),
            textAlign = TextAlign.Center,
            style = AppTheme.typography.titleLarge,
            fontWeight = FontWeight.Medium,
            color = AppTheme.colors.onBackground,
        )

        Spacer(modifier = Modifier.weight(1f))

        AppButton(
            text = stringResource(DesignR.string.try_again),
            onClick = { onIntent(Intent.OnNextButtonClick) },
            modifier = Modifier
                .smallDeviceMaxWidth()
                .fillMaxWidth()
        )
    }
}

@Composable
private fun ScreenSideEffects(
    sideEffects: Flow<SideEffect>,
    onIntent: (Intent) -> Unit,
) {
    val context = LocalContext.current
    val snackbarHostState = LocalAppScaffoldController.current.snackbarHostState

    val recordAudioPermissionLauncher = rememberPermissionState(
        permission = Manifest.permission.RECORD_AUDIO
    )

    ScreenSideEffectsFlowHandler(sideEffects = sideEffects) { sideEffect ->
        when (sideEffect) {
            is SideEffect.Message -> {
                handleMessageSideEffect(
                    message = sideEffect.text,
                    snackbarHostState = snackbarHostState,
                    context = context,
                )
            }

            is SideEffect.RequestAudioRecordPermission -> {
                if (recordAudioPermissionLauncher.status.isGranted) {
                    onIntent(Intent.OnAudioRecordPermissionGranted)
                } else {
                    recordAudioPermissionLauncher.launchPermissionRequest()
                }
            }
        }
    }
}

@Composable
private fun State.Resolving.Step.buttonColors() =
    when (this) {
        State.Resolving.Step.Success ->
            AppButtonDefaults.colors(
                idleColor = AppTheme.colors.success,
                idleContentColor = AppTheme.colors.onSuccess,
            )
        State.Resolving.Step.Error ->
            AppButtonDefaults.colors(
                idleColor = AppTheme.colors.error,
                idleContentColor = AppTheme.colors.onError,
            )
        else ->
            AppButtonDefaults.colors()
    }

@Composable
private fun State.Resolving.Step.buttonText() =
    stringResource(
        when (this) {
            State.Resolving.Step.None -> R.string.audition_check_speech
            State.Resolving.Step.Listening -> R.string.audition_listening
            State.Resolving.Step.Loading -> R.string.audition_listening
            State.Resolving.Step.Success -> R.string.audition_success
            State.Resolving.Step.Error -> R.string.audition_error
        }
    )

@Composable
private fun State.Resolving.Step.buttonScale(): androidx.compose.runtime.State<Float> {
    val infiniteTransition = rememberInfiniteTransition(
        label = "AuditionButtonScale"
    )

    val targetButtonScale = remember(this) {
        if (this == State.Resolving.Step.Listening) {
            AUDITION_BUTTON_LISTENING_SCALE
        } else {
            AUDITION_BUTTON_SCALE
        }
    }

    return infiniteTransition.animateFloat(
        initialValue = AUDITION_BUTTON_SCALE,
        targetValue = targetButtonScale,
        label = "AuditionButtonScale",
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = AUDITION_BUTTON_ANIMATION_DURATION_MILLIS,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
    )
}

@Composable
private fun State.Resolving.Step.buttonState() =
    when (this) {
        State.Resolving.Step.Loading -> AppButtonState.Loading
        else -> AppButtonState.Idle
    }

private const val AUDITION_BUTTON_SCALE = 1f
private const val AUDITION_BUTTON_LISTENING_SCALE = 0.95f
private const val AUDITION_BUTTON_ANIMATION_DURATION_MILLIS = 500

private class PreviewStateProvider : PreviewParameterProvider<State> {
    override val values = sequenceOf(
        State.Loading,
        State.Resolving(
            word = "gardener",
            wordTranscription = "[ 'gɑ:dnə ]",
        ),
        State.Error
    )
}

@Composable
private fun AuditionScreenPreview(state: State) {
    AppRootContainer { _, _ ->
        WordPracticeScreen(
            state = state,
            onIntent = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun AuditionScreenPreviewLight(
    @PreviewParameter(PreviewStateProvider::class) state: State
) {
    AuditionScreenPreview(state = state)
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun AuditionScreenPreviewDark(
    @PreviewParameter(PreviewStateProvider::class) state: State
) {
    AuditionScreenPreview(state = state)
}

@Composable
@Preview(
    showBackground = true,
    device = COMPOSE_LARGE_DEVICE_SPEC,
)
private fun AuditionScreenPreviewLightLarge(
    @PreviewParameter(PreviewStateProvider::class) state: State
) {
    AuditionScreenPreview(state = state)
}

@Composable
@Preview(
    showBackground = true,
    device = COMPOSE_LARGE_DEVICE_SPEC,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
private fun AuditionScreenPreviewDarkLarge(
    @PreviewParameter(PreviewStateProvider::class) state: State
) {
    AuditionScreenPreview(state = state)
}
