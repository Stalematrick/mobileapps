package ru.abramovkirill.languageapp.profile.impl.ui.screens.select_language

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import ru.abramovkirill.languageapp.common.utils.compose.ScreenSideEffectsFlowHandler
import ru.abramovkirill.languageapp.common.utils.compose.handleMessageSideEffect
import ru.abramovkirill.languageapp.common.utils.extract
import ru.abramovkirill.languageapp.common.utils.strRes
import ru.abramovkirill.languageapp.core.design.composables.AppBar
import ru.abramovkirill.languageapp.core.design.composables.AppBarTitleGravity
import ru.abramovkirill.languageapp.core.design.composables.AppRootContainer
import ru.abramovkirill.languageapp.core.design.composables.button.AppButton
import ru.abramovkirill.languageapp.core.design.composables.button.AppButtonState
import ru.abramovkirill.languageapp.core.design.composables.scaffold.AppBarState
import ru.abramovkirill.languageapp.core.design.composables.scaffold.AppScaffoldControllerEffect
import ru.abramovkirill.languageapp.core.design.composables.scaffold.LocalAppScaffoldController
import ru.abramovkirill.languageapp.core.design.composables.shimmerEffect
import ru.abramovkirill.languageapp.core.design.styles.AppTheme
import ru.abramovkirill.languageapp.core.design.utils.COMPOSE_LARGE_DEVICE_SPEC
import ru.abramovkirill.languageapp.core.design.utils.smallDeviceMaxWidth
import ru.abramovkirill.languageapp.profile.api.ui.navigation.SELECT_LANGUAGE_SCREEN_ROUTE_PATH
import ru.abramovkirill.languageapp.profile.impl.R
import ru.abramovkirill.languageapp.profile.impl.ui.screens.select_language.SelectLanguageContract.Intent
import ru.abramovkirill.languageapp.profile.impl.ui.screens.select_language.SelectLanguageContract.SideEffect
import ru.abramovkirill.languageapp.profile.impl.ui.screens.select_language.SelectLanguageContract.State
import ru.abramovkirill.languageapp.core.design.R as DesignR

@Composable
internal fun SelectLanguageScreen(viewModel: SelectLanguageViewModel) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val sideEffects = viewModel.container.sideEffectFlow

    SelectLanguageSideEffects(sideEffects = sideEffects)

    SelectLanguageScreen(
        state = state,
        onIntent = viewModel::processIntent
    )
}

@Composable
private fun SelectLanguageScreen(
    state: State,
    onIntent: (Intent) -> Unit,
) {
    AppScaffoldControllerEffect(
        appBarState = AppBarState.Custom(key = SELECT_LANGUAGE_SCREEN_ROUTE_PATH) {
            AppBar(
                title = stringResource(R.string.language_select_title),
                titleGravity = AppBarTitleGravity.CENTER,
                onGoBackClick = if (state.canGoBack) {
                    { onIntent(Intent.OnGoBackClick) }
                } else {
                    null
                }
            )
        }
    )

    val density = LocalDensity.current

    var chooseButtonTopPx by remember { mutableFloatStateOf(0f) }

    val chooseButtonTopDp = remember(chooseButtonTopPx, density) {
        with(density) { chooseButtonTopPx.toDp() + 12.dp }
    }

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            contentPadding = PaddingValues(
                start = 24.dp,
                end = 24.dp,
                top = 12.dp,
                bottom  = 12.dp + chooseButtonTopDp,
            ),
            modifier = Modifier
                .smallDeviceMaxWidth()
                .fillMaxSize()
        ) {
            titleItem()

            when (state) {
                is State.Loading ->
                    shimmerItems()
                is State.Loaded ->
                    languagesItems(
                        languages = state.languages,
                        onClick = { id -> onIntent(Intent.OnLanguageSelect(id)) }
                    )
            }
        }

        SelectLanguageChooseButton(
            state = state,
            onClick = { onIntent(Intent.OnChooseButtonClick) },
            modifier = Modifier
                .smallDeviceMaxWidth()
                .onGloballyPositioned { coordinates ->
                    chooseButtonTopPx = coordinates.boundsInParent().top
                }
                .fillMaxWidth()
                .padding(24.dp)
        )
    }
}

private fun LazyListScope.titleItem() {
    item {
        Text(
            text = stringResource(R.string.language_select_what_is_your_mother_language),
            style = AppTheme.typography.titleLarge,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = AppTheme.colors.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

private fun LazyListScope.languagesItems(
    languages: List<LanguageItem>,
    onClick: (id: String) -> Unit
) {
    itemsIndexed(
        items = languages,
        key = { _, item -> item.id }
    ) { index, item ->
        val backgroundColor = if (item.isSelected) {
            AppTheme.colors.primaryContainer
        } else {
            AppTheme.colors.primaryContainer.copy(alpha = 0.1f)
        }

        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(AppTheme.shapes.extraLarge)
                .clickable { onClick(item.id) }
                .background(backgroundColor)
                .padding(
                    horizontal = 16.dp,
                    vertical = 19.dp,
                )
        ) {
            Text(
                text = item.name.extract() ?: "-",
                style = AppTheme.typography.bodyLarge,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = AppTheme.colors.onBackground,
            )
        }

        if (index != languages.lastIndex) {
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

private fun LazyListScope.shimmerItems() {
    items(SELECT_LANGUAGE_SHIMMER_ITEMS_COUNT) { index ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(67.dp)
                .clip(AppTheme.shapes.extraLarge)
                .shimmerEffect()
        )

        if (index != SELECT_LANGUAGE_SHIMMER_ITEMS_COUNT - 1) {
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun SelectLanguageChooseButton(
    state: State,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AppButton(
        text = stringResource(DesignR.string.choose),
        buttonState = when {
            state is State.Loaded && state.isChoosingLanguage ->
                AppButtonState.Loading
            state is State.Loaded ->
                AppButtonState.Idle
            state is State.Loading ->
                AppButtonState.Loading
            else ->
                AppButtonState.Disabled
        },
        onClick = onClick,
        modifier = modifier
    )
}

@Composable
private fun SelectLanguageSideEffects(
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

private const val SELECT_LANGUAGE_SHIMMER_ITEMS_COUNT = 5

private class SelectLanguagePreviewStateProvider : PreviewParameterProvider<State> {
    override val values = sequenceOf(
        State.Loading(canGoBack = true),
        State.Loaded(
            canGoBack = true,
            languages = persistentListOf(
                LanguageItem(
                    id = "1",
                    name = strRes("Russian"),
                    isSelected = true,
                ),
                LanguageItem(
                    id = "2",
                    name = strRes("English"),
                    isSelected = false,
                )
            )
        )
    )
}

@Composable
private fun SelectLanguageScreenPreview(
    state: State
) {
    AppRootContainer { _, _ ->
        SelectLanguageScreen(
            state = state,
            onIntent = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun SelectLanguageScreenPreviewLight(
    @PreviewParameter(SelectLanguagePreviewStateProvider::class) state: State
) {
    SelectLanguageScreenPreview(state)
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun SelectLanguageScreenPreviewDark(
    @PreviewParameter(SelectLanguagePreviewStateProvider::class) state: State
) {
    SelectLanguageScreenPreview(state)
}

@Composable
@Preview(
    showBackground = true,
    device = COMPOSE_LARGE_DEVICE_SPEC,
)
private fun SelectLanguageScreenPreviewLightLarge(
    @PreviewParameter(SelectLanguagePreviewStateProvider::class) state: State
) {
    SelectLanguageScreenPreview(state)
}

@Composable
@Preview(
    showBackground = true,
    device = COMPOSE_LARGE_DEVICE_SPEC,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
private fun SelectLanguageScreenPreviewDarkLarge(
    @PreviewParameter(SelectLanguagePreviewStateProvider::class) state: State
) {
    SelectLanguageScreenPreview(state)
}
