package ru.abramovkirill.languageapp.auth.impl.ui.screens.recovery

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import ru.abramovkirill.languageapp.auth.impl.R
import ru.abramovkirill.languageapp.auth.impl.di.AuthComponent
import ru.abramovkirill.languageapp.auth.impl.ui.navigation.recovery.RECOVERY_FLOW_ROUTE_PATH
import ru.abramovkirill.languageapp.auth.impl.ui.navigation.recovery.RecoveryGraphRoute
import ru.abramovkirill.languageapp.auth.impl.ui.screens.recovery.RecoveryFlowContract.Intent
import ru.abramovkirill.languageapp.auth.impl.ui.screens.recovery.RecoveryFlowContract.SideEffect
import ru.abramovkirill.languageapp.auth.impl.ui.screens.recovery.RecoveryFlowContract.State
import ru.abramovkirill.languageapp.auth.impl.ui.screens.recovery.check_email.RecoveryCheckEmailScreen
import ru.abramovkirill.languageapp.auth.impl.ui.screens.recovery.choose_password.RecoveryChoosePasswordScreen
import ru.abramovkirill.languageapp.auth.impl.ui.screens.recovery.enter_email.RecoveryEnterEmailScreen
import ru.abramovkirill.languageapp.auth.impl.ui.screens.recovery.error.RecoveryErrorScreen
import ru.abramovkirill.languageapp.auth.impl.ui.screens.recovery.success.RecoverySuccessScreen
import ru.abramovkirill.languageapp.common.utils.compose.ScreenSideEffectsFlowHandler
import ru.abramovkirill.languageapp.common.utils.compose.handleMessageSideEffect
import ru.abramovkirill.languageapp.core.design.composables.AppBar
import ru.abramovkirill.languageapp.core.design.composables.AppBarTitleGravity
import ru.abramovkirill.languageapp.core.design.composables.AppRootContainer
import ru.abramovkirill.languageapp.core.design.composables.button.AppButton
import ru.abramovkirill.languageapp.core.design.composables.button.toButtonState
import ru.abramovkirill.languageapp.core.design.composables.scaffold.AppBarState
import ru.abramovkirill.languageapp.core.design.composables.scaffold.AppScaffoldControllerEffect
import ru.abramovkirill.languageapp.core.design.composables.scaffold.LocalAppScaffoldController
import ru.abramovkirill.languageapp.core.design.utils.smallDeviceMaxWidth
import ru.abramovkirill.languageapp.core.navigation.compose_impl.ComposeRouter
import ru.abramovkirill.languageapp.core.navigation.compose_impl.NavHost

@Composable
fun RecoveryFlow(viewModel: RecoveryFlowViewModel) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()

    RecoveryFlowSideEffects(sideEffects = viewModel.container.sideEffectFlow)

    RecoveryFlow(
        state = state,
        onIntent = viewModel::processIntent
    ) {
        NavHost(
            navController = navController,
            graph = RecoveryGraphRoute(
                recoveryViewModelProvider = { viewModel }
            ),
        )

        RecoveryLaunchedRouterEffect(
            coroutineScope = coroutineScope,
            navController = navController
        )
    }
}

@Composable
private fun RecoveryFlow(
    state: State,
    onIntent : (Intent) -> Unit,
    content: @Composable () -> Unit,
) {
    val buttonsController = rememberRecoveryButtonsController()

    AppScaffoldControllerEffect(
        appBarState = AppBarState.Custom(key = RECOVERY_FLOW_ROUTE_PATH) {
            AppBar(
                title = stringResource(R.string.password_recovery),
                titleGravity = AppBarTitleGravity.CENTER,
                onGoBackClick = { onIntent(Intent.OnGoBackClick) },
            )
        }
    )

    Column(
        modifier = Modifier
            .padding(vertical = 40.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))

        CompositionLocalProvider(
            value = LocalRecoveryButtonsController provides buttonsController,
            content = content,
        )

        Spacer(modifier = Modifier.weight(1f))

        AppButton(
            text = buttonsController.text,
            buttonState = state.recoveringState.toButtonState(),
            onClick = buttonsController.onClick,
            modifier = Modifier
                .smallDeviceMaxWidth()
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )
    }
}

@Composable
private fun RecoveryLaunchedRouterEffect(
    coroutineScope: CoroutineScope,
    navController: NavController,
    router: ComposeRouter = AuthComponent.recoveryRouter,
) {
    DisposableEffect(coroutineScope, navController, router) {
        router.attachNavController(
            navController = navController,
            coroutineScope = coroutineScope
        )

        onDispose { router.detachNavController() }
    }
}

@Composable
private fun RecoveryFlowSideEffects(
    sideEffects: Flow<SideEffect>,
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
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

            is SideEffect.CloseKeyboard -> {
                keyboardController?.hide()
            }
        }
    }
}

internal class RecoveryButtonsController {
    var text by mutableStateOf("")
    var onClick by mutableStateOf({})
}

internal val LocalRecoveryButtonsController =
    staticCompositionLocalOf { RecoveryButtonsController() }

@Composable
internal fun rememberRecoveryButtonsController() =
    remember { RecoveryButtonsController() }

@Composable
internal fun RecoveryButtonsControllerEffect(
    text: String,
    onClick: () -> Unit,
) {
    val recoveryButtonsController = LocalRecoveryButtonsController.current

    LaunchedEffect(recoveryButtonsController) {
        recoveryButtonsController.text = text
        recoveryButtonsController.onClick = onClick
    }
}

@Composable
private fun RecoveryFlowPreview(
    content: @Composable (State) -> Unit,
) {
    val state = State()

    AppRootContainer { _, _ ->
        RecoveryFlow(
            state = state,
            onIntent = {}
        ) {
            content(state)
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun RecoveryFlowPreviewEnterEmail() {
    RecoveryFlowPreview { state ->
        RecoveryEnterEmailScreen(
            state = state,
            onIntent = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun RecoveryFlowPreviewCheckEmail() {
    RecoveryFlowPreview { state ->
        RecoveryCheckEmailScreen(
            state = state,
            onIntent = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun RecoveryFlowPreviewChoosePassword() {
    RecoveryFlowPreview { state ->
        RecoveryChoosePasswordScreen(
            state = state,
            onIntent = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun RecoveryFlowPreviewSuccess() {
    RecoveryFlowPreview {
        RecoverySuccessScreen(
            onIntent = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun RecoveryFlowPreviewError() {
    RecoveryFlowPreview {
        RecoveryErrorScreen(
            onIntent = {}
        )
    }
}
