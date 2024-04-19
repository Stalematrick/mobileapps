package ru.abramovkirill.languageapp.exercises.word_practice.impl.ui.screens.word_practice

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import ru.abramovkirill.languageapp.common.utils.compose.ScreenSideEffectsFlowHandler
import ru.abramovkirill.languageapp.common.utils.compose.handleMessageSideEffect
import ru.abramovkirill.languageapp.core.design.composables.AppBar
import ru.abramovkirill.languageapp.core.design.composables.AppRootContainer
import ru.abramovkirill.languageapp.core.design.composables.button.AppButton
import ru.abramovkirill.languageapp.core.design.composables.button.AppButtonDefaults
import ru.abramovkirill.languageapp.core.design.composables.button.AppButtonState
import ru.abramovkirill.languageapp.core.design.composables.button.toButtonState
import ru.abramovkirill.languageapp.core.design.composables.scaffold.AppBarState
import ru.abramovkirill.languageapp.core.design.composables.scaffold.AppScaffoldControllerEffect
import ru.abramovkirill.languageapp.core.design.composables.scaffold.LocalAppScaffoldController
import ru.abramovkirill.languageapp.core.design.composables.shimmerEffect
import ru.abramovkirill.languageapp.core.design.styles.AppTheme
import ru.abramovkirill.languageapp.core.design.utils.COMPOSE_LARGE_DEVICE_SPEC
import ru.abramovkirill.languageapp.core.design.utils.smallDeviceMaxWidth
import ru.abramovkirill.languageapp.exercises.word_practice.api.ui.navigation.WORD_PRACTICE_SCREEN_ROUTE_PATH
import ru.abramovkirill.languageapp.exercises.word_practice.impl.R
import ru.abramovkirill.languageapp.exercises.word_practice.impl.ui.screens.word_practice.WordPracticeContract.Intent
import ru.abramovkirill.languageapp.exercises.word_practice.impl.ui.screens.word_practice.WordPracticeContract.SideEffect
import ru.abramovkirill.languageapp.exercises.word_practice.impl.ui.screens.word_practice.WordPracticeContract.State
import ru.abramovkirill.languageapp.core.design.R as DesignR

@Composable
internal fun WordPracticeScreen(viewModel: WordPracticeViewModel) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val sideEffects = viewModel.container.sideEffectFlow

    ScreenSideEffects(sideEffects = sideEffects)

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
        appBarState = AppBarState.Custom(key = WORD_PRACTICE_SCREEN_ROUTE_PATH) {
            AppBar(
                title = stringResource(R.string.word_practice_title),
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

        LazyColumn(
            modifier = Modifier
                .smallDeviceMaxWidth()
                .fillMaxWidth()
                .weight(1f)
        ) {
            itemsIndexed(
                items = state.answers,
                key = { _, item -> item.word }
            ) { index, item ->
                ResolvingAnswerItem(
                    answer = item,
                    isEnabled = !state.isResolved,
                    onClick = { onIntent(Intent.OnAnswerClick(item)) },
                    modifier = Modifier
                        .fillMaxWidth()
                )

                if (index != state.answers.lastIndex) {
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }

        ResolvingButton(
            isResolved = state.isResolved,
            buttonState = state.checkingAnswerState.toButtonState(),
            onNextButtonClick = { onIntent(Intent.OnNextButtonClick) },
            onCheckButtonClick = { onIntent(Intent.OnCheckButtonClick) },
            modifier = Modifier
                .smallDeviceMaxWidth()
                .fillMaxWidth()
        )
    }
}

@Composable
private fun ResolvingAnswerItem(
    answer: WordPracticeAnswerItem,
    isEnabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonColor = when (answer.selectionType) {
        WordPracticeAnswerSelectionType.User ->
            AppTheme.colors.secondary
        WordPracticeAnswerSelectionType.Correct ->
            AppTheme.colors.success
        WordPracticeAnswerSelectionType.Incorrect ->
            AppTheme.colors.error
        WordPracticeAnswerSelectionType.None ->
            AppTheme.colors.surface
    }

    val buttonContentColor = when (answer.selectionType) {
        WordPracticeAnswerSelectionType.User ->
            AppTheme.colors.onSecondary
        WordPracticeAnswerSelectionType.Correct ->
            AppTheme.colors.onSuccess
        WordPracticeAnswerSelectionType.Incorrect ->
            AppTheme.colors.onError
        WordPracticeAnswerSelectionType.None ->
            AppTheme.colors.onSurface
    }

    AppButton(
        text = answer.word,
        lightingEnabled =
            answer.selectionType != WordPracticeAnswerSelectionType.None,
        buttonState = if (isEnabled) {
            AppButtonState.Idle
        } else {
            AppButtonState.Disabled
        },
        buttonColors = AppButtonDefaults.colors(
            idleColor = buttonColor,
            idleContentColor = buttonContentColor,
            disabledColor = buttonColor,
            disabledContentColor = buttonContentColor,
        ),
        onClick = onClick,
        modifier = modifier,
    )
}

@Composable
private fun ResolvingButton(
    isResolved: Boolean,
    buttonState: AppButtonState,
    onNextButtonClick: () -> Unit,
    onCheckButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AppButton(
        text = if (isResolved) {
            stringResource(DesignR.string.next)
        } else {
            stringResource(DesignR.string.check)
        },
        buttonState = buttonState,
        onClick = if (isResolved) onNextButtonClick else onCheckButtonClick,
        modifier = modifier
    )
}

@Composable
private fun ScreenShimmer() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Box(
            modifier = Modifier
                .size(width = 100.dp, height = 28.dp)
                .clip(AppTheme.shapes.small)
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.height(2.dp))

        Box(
            modifier = Modifier
                .size(width = 85.dp, height = 17.dp)
                .clip(AppTheme.shapes.small)
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.height(35.dp))

        repeat(SHIMMER_ANSWERS_COUNT) {
            Box(
                modifier = Modifier
                    .smallDeviceMaxWidth()
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(AppTheme.shapes.medium)
                    .shimmerEffect()
            )

            Spacer(modifier = Modifier.height(10.dp))
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .smallDeviceMaxWidth()
                .fillMaxWidth()
                .height(56.dp)
                .clip(AppTheme.shapes.medium)
                .shimmerEffect()
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
) {
    val context = LocalContext.current
    val snackbarHostState = LocalAppScaffoldController.current.snackbarHostState

    ScreenSideEffectsFlowHandler(sideEffects = sideEffects) { sideEffect ->
        when (sideEffect) {
            is SideEffect.Message -> {
                handleMessageSideEffect(
                    message = sideEffect.text,
                    snackbarHostState = snackbarHostState,
                    context = context,
                )
            }
        }
    }
}

private const val SHIMMER_ANSWERS_COUNT = 4

private class PreviewStateProvider : PreviewParameterProvider<State> {
    override val values = sequenceOf(
        State.Loading,
        State.Resolving(
            word = "gardener",
            wordTranscription = "[ 'gɑ:dnə ]",
            answers = persistentListOf(
                WordPracticeAnswerItem(
                    word = "Муха",
                ),
                WordPracticeAnswerItem(
                    word = "Садовник",
                    selectionType = WordPracticeAnswerSelectionType.Correct,
                ),
                WordPracticeAnswerItem(
                    word = "Гладиолус",
                    selectionType = WordPracticeAnswerSelectionType.User,
                ),
                WordPracticeAnswerItem(
                    word = "Собака",
                ),
            )
        ),
        State.Error
    )
}

@Composable
private fun WordPracticeScreenPreview(state: State) {
    AppRootContainer { _, _ ->
        WordPracticeScreen(
            state = state,
            onIntent = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun WordPracticeScreenPreviewLight(
    @PreviewParameter(PreviewStateProvider::class) state: State
) {
    WordPracticeScreenPreview(state = state)
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun WordPracticeScreenPreviewDark(
    @PreviewParameter(PreviewStateProvider::class) state: State
) {
    WordPracticeScreenPreview(state = state)
}

@Composable
@Preview(
    showBackground = true,
    device = COMPOSE_LARGE_DEVICE_SPEC,
)
private fun WordPracticeScreenPreviewLightLarge(
    @PreviewParameter(PreviewStateProvider::class) state: State
) {
    WordPracticeScreenPreview(state = state)
}

@Composable
@Preview(
    showBackground = true,
    device = COMPOSE_LARGE_DEVICE_SPEC,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
private fun WordPracticeScreenPreviewDarkLarge(
    @PreviewParameter(PreviewStateProvider::class) state: State
) {
    WordPracticeScreenPreview(state = state)
}
