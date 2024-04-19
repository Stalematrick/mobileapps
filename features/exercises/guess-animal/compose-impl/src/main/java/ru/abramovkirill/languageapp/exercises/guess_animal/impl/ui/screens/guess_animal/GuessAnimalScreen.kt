package ru.abramovkirill.languageapp.exercises.guess_animal.impl.ui.screens.guess_animal

import android.content.res.Configuration
import androidx.activity.SystemBarStyle
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.abramovkirill.languageapp.common.utils.compose.ScreenSideEffectsFlowHandler
import ru.abramovkirill.languageapp.common.utils.compose.handleMessageSideEffect
import ru.abramovkirill.languageapp.common.utils.painterRes
import ru.abramovkirill.languageapp.core.design.composables.AppBar
import ru.abramovkirill.languageapp.core.design.composables.AppPainterWrapper
import ru.abramovkirill.languageapp.core.design.composables.AppRootContainer
import ru.abramovkirill.languageapp.core.design.composables.animation.FadeInAnimatedContent
import ru.abramovkirill.languageapp.core.design.composables.button.AppButton
import ru.abramovkirill.languageapp.core.design.composables.button.toButtonState
import ru.abramovkirill.languageapp.core.design.composables.scaffold.AppBarState
import ru.abramovkirill.languageapp.core.design.composables.scaffold.AppScaffoldControllerEffect
import ru.abramovkirill.languageapp.core.design.composables.scaffold.LocalAppScaffoldController
import ru.abramovkirill.languageapp.core.design.composables.scaffold.LocalAppSystemBarsStylesDefault
import ru.abramovkirill.languageapp.core.design.composables.scaffold.darkTransparent
import ru.abramovkirill.languageapp.core.design.composables.shimmerEffect
import ru.abramovkirill.languageapp.core.design.composables.text_field.AppTextField
import ru.abramovkirill.languageapp.core.design.styles.AppTheme
import ru.abramovkirill.languageapp.core.design.utils.COMPOSE_LARGE_DEVICE_SPEC
import ru.abramovkirill.languageapp.core.design.utils.smallDeviceMaxWidth
import ru.abramovkirill.languageapp.exercises.guess_animal.api.ui.navigation.GUESS_ANIMAL_SCREEN_ROUTE_PATH
import ru.abramovkirill.languageapp.exercises.guess_animal.impl.R
import ru.abramovkirill.languageapp.exercises.guess_animal.impl.ui.screens.guess_animal.GuessAnimalContract.Intent
import ru.abramovkirill.languageapp.exercises.guess_animal.impl.ui.screens.guess_animal.GuessAnimalContract.SideEffect
import ru.abramovkirill.languageapp.exercises.guess_animal.impl.ui.screens.guess_animal.GuessAnimalContract.State
import ru.abramovkirill.languageapp.core.design.R as DesignR

@Composable
internal fun GuessAnimalScreen(viewModel: GuessAnimalViewModel) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val sideEffects = viewModel.container.sideEffectFlow

    ScreenSideEffects(sideEffects = sideEffects)

    GuessAnimalScreen(
        state = state,
        onIntent = viewModel::processIntent
    )
}

@Composable
private fun GuessAnimalScreen(
    state: State,
    onIntent: (Intent) -> Unit,
) {
    AppScaffoldControllerEffect(
        appBarState = AppBarState.Custom(key = GUESS_ANIMAL_SCREEN_ROUTE_PATH) {
            AppBar(
                title = stringResource(R.string.guess_animal_title),
                containerColor = when (state) {
                    is State.CorrectAnswer ->
                        AppTheme.colors.success
                    is State.IncorrectAnswer ->
                        AppTheme.colors.error
                    else ->
                        AppTheme.colors.primary
                },
                onGoBackClick = { onIntent(Intent.OnGoBackClick) }
            )
        },
        appSystemBarsStyles = LocalAppSystemBarsStylesDefault.current.copy(
            key = "$GUESS_ANIMAL_SCREEN_ROUTE_PATH/${state::class.simpleName}",
            statusBar = SystemBarStyle.darkTransparent()
        )
    )

    FadeInAnimatedContent(
        targetState = state,
        contentKey = { contentState -> contentState::class },
        label = "GuessAnimalContentAnimation"
    ) { contentState ->
        when (contentState) {
            State.Loading ->
                ScreenShimmer()
            State.CorrectAnswer ->
                ScreenCorrectAnswer(
                    onIntent = onIntent,
                )
            is State.IncorrectAnswer ->
                ScreenIncorrectAnswer(
                    state = contentState,
                    onIntent = onIntent,
                )
            is State.Resolving ->
                ScreenResolving(
                    state = contentState,
                    onIntent = onIntent
                )
            State.Error ->
                ScreenError(
                    onIntent = onIntent
                )
        }
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
        AppPainterWrapper(
            painterResource = state.image,
            successContent = { painter ->
                Image(
                    painter = painter,
                    contentScale = ContentScale.FillWidth,
                    contentDescription = null,
                    modifier = Modifier
                        .smallDeviceMaxWidth()
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clip(AppTheme.shapes.medium),
                )
            },
            loadingContent = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(328.dp)
                        .clip(AppTheme.shapes.medium)
                        .shimmerEffect()
                )
            }
        )


        Spacer(modifier = Modifier.height(16.dp))

        AppTextField(
            value = state.answer,
            placeholder = stringResource(R.string.guess_animal_input_placeholder),
            label = stringResource(R.string.guess_animal_input_label),
            singleLine = true,
            onValueChange = { onIntent(Intent.OnAnswerTextChange(it)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { onIntent(Intent.OnCheckButtonClick) }
            ),
            modifier = Modifier
                .smallDeviceMaxWidth(),
        )

        Spacer(modifier = Modifier.height(17.dp))

        AppButton(
            text = stringResource(DesignR.string.check),
            buttonState = state.checkingAnswerState.toButtonState(),
            onClick = { onIntent(Intent.OnCheckButtonClick) },
            modifier = Modifier
                .smallDeviceMaxWidth()
                .fillMaxWidth()
        )
    }
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
                .smallDeviceMaxWidth()
                .fillMaxWidth()
                .height(328.dp)
                .clip(AppTheme.shapes.medium)
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .smallDeviceMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(width = 100.dp, height = 15.dp)
                    .clip(AppTheme.shapes.small)
                    .shimmerEffect()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(AppTheme.shapes.medium)
                    .shimmerEffect()
            )
        }


        Spacer(modifier = Modifier.height(17.dp))

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
private fun ScreenCorrectAnswer(
    onIntent: (Intent) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Image(
            painter = painterResource(DesignR.drawable.image_success),
            contentScale = ContentScale.FillWidth,
            contentDescription = null,
            modifier = Modifier
                .width(160.dp)
                .clip(AppTheme.shapes.medium),
        )

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = stringResource(R.string.guess_animal_success),
            textAlign = TextAlign.Center,
            style = AppTheme.typography.titleLarge,
            fontWeight = FontWeight.Medium,
            color = AppTheme.colors.onBackground,
        )

        Spacer(modifier = Modifier.height(40.dp))

        AppButton(
            text = stringResource(DesignR.string.next),
            onClick = { onIntent(Intent.OnNextButtonClick) },
            modifier = Modifier
                .smallDeviceMaxWidth()
                .fillMaxWidth()
        )
    }
}

@Composable
private fun ScreenIncorrectAnswer(
    state: State.IncorrectAnswer,
    onIntent: (Intent) -> Unit,
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
            text = stringResource(R.string.guess_animal_error_formatted, state.correctAnswer),
            textAlign = TextAlign.Center,
            style = AppTheme.typography.titleLarge,
            fontWeight = FontWeight.Medium,
            color = AppTheme.colors.onBackground,
        )

        Spacer(modifier = Modifier.height(18.dp))

        AppButton(
            text = stringResource(DesignR.string.next),
            onClick = { onIntent(Intent.OnNextButtonClick) },
            modifier = Modifier
                .smallDeviceMaxWidth()
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(11.dp))

        AppButton(
            text = stringResource(DesignR.string.try_again),
            onClick = { onIntent(Intent.OnTryAgainButtonClick) },
            modifier = Modifier
                .smallDeviceMaxWidth()
                .fillMaxWidth()
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

        Spacer(modifier = Modifier.height(18.dp))

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
    val keyboardController = LocalSoftwareKeyboardController.current

    ScreenSideEffectsFlowHandler(sideEffects = sideEffects) { sideEffect ->
        when (sideEffect) {
            is SideEffect.Message -> {
                handleMessageSideEffect(
                    message = sideEffect.text,
                    snackbarHostState = snackbarHostState,
                    context = context,
                )
            }

            SideEffect.CloseKeyboard -> {
                keyboardController?.hide()
            }
        }
    }
}

private class PreviewStateProvider : PreviewParameterProvider<State> {
    override val values = sequenceOf(
        State.Loading,
        State.Resolving(
            image = painterRes(R.drawable.guess_animal_preview),
            answer = "",
        ),
        State.CorrectAnswer,
        State.IncorrectAnswer(
            correctAnswer = "raccoon"
        ),
        State.Error
    )
}

@Composable
private fun GuessAnimalScreenPreview(state: State) {
    AppRootContainer { _, _ ->
        GuessAnimalScreen(
            state = state,
            onIntent = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun GuessAnimalScreenPreviewLight(
    @PreviewParameter(PreviewStateProvider::class) state: State
) {
    GuessAnimalScreenPreview(state = state)
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun GuessAnimalScreenPreviewDark(
    @PreviewParameter(PreviewStateProvider::class) state: State
) {
    GuessAnimalScreenPreview(state = state)
}

@Composable
@Preview(
    showBackground = true,
    device = COMPOSE_LARGE_DEVICE_SPEC,
)
private fun GuessAnimalScreenPreviewLightLarge(
    @PreviewParameter(PreviewStateProvider::class) state: State
) {
    GuessAnimalScreenPreview(state = state)
}

@Composable
@Preview(
    showBackground = true,
    device = COMPOSE_LARGE_DEVICE_SPEC,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
private fun GuessAnimalScreenPreviewDarkLarge(
    @PreviewParameter(PreviewStateProvider::class) state: State
) {
    GuessAnimalScreenPreview(state = state)
}
