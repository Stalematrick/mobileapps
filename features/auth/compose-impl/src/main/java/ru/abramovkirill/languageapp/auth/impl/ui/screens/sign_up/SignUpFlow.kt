package ru.abramovkirill.languageapp.auth.impl.ui.screens.sign_up

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import ru.abramovkirill.languageapp.auth.impl.R
import ru.abramovkirill.languageapp.auth.impl.di.AuthComponent
import ru.abramovkirill.languageapp.auth.impl.ui.navigation.sign_up.SIGN_UP_GRAPH_ROUTE_PATH
import ru.abramovkirill.languageapp.auth.impl.ui.navigation.sign_up.SignUpGraphRoute
import ru.abramovkirill.languageapp.auth.impl.ui.screens.sign_up.SignUpScreenContract.Intent
import ru.abramovkirill.languageapp.auth.impl.ui.screens.sign_up.SignUpScreenContract.SideEffect
import ru.abramovkirill.languageapp.auth.impl.ui.screens.sign_up.SignUpScreenContract.State
import ru.abramovkirill.languageapp.auth.impl.ui.screens.sign_up.email_confirm.SignUpEmailConfirmScreen
import ru.abramovkirill.languageapp.auth.impl.ui.screens.sign_up.general_info.SignUpGeneralInfoScreen
import ru.abramovkirill.languageapp.auth.impl.ui.screens.sign_up.password.SignUpPasswordScreen
import ru.abramovkirill.languageapp.common.utils.compose.ScreenSideEffectsFlowHandler
import ru.abramovkirill.languageapp.common.utils.compose.handleMessageSideEffect
import ru.abramovkirill.languageapp.common.utils.states.ProcessingState
import ru.abramovkirill.languageapp.core.design.composables.AppBar
import ru.abramovkirill.languageapp.core.design.composables.AppBarTitleGravity
import ru.abramovkirill.languageapp.core.design.composables.AppLinkedText
import ru.abramovkirill.languageapp.core.design.composables.AppLinkedTextUnit
import ru.abramovkirill.languageapp.core.design.composables.AppRootContainer
import ru.abramovkirill.languageapp.core.design.composables.button.AppButton
import ru.abramovkirill.languageapp.core.design.composables.button.toButtonState
import ru.abramovkirill.languageapp.core.design.composables.scaffold.AppBarState
import ru.abramovkirill.languageapp.core.design.composables.scaffold.AppScaffoldControllerEffect
import ru.abramovkirill.languageapp.core.design.composables.scaffold.LocalAppScaffoldController
import ru.abramovkirill.languageapp.core.design.styles.AppTheme
import ru.abramovkirill.languageapp.core.design.utils.smallDeviceMaxWidth
import ru.abramovkirill.languageapp.core.navigation.compose_impl.ComposeRouter
import ru.abramovkirill.languageapp.core.navigation.compose_impl.NavHost

@Composable
fun SignUpFlow(viewModel: SignUpViewModel) {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    SignUpScreenSideEffects(
        sideEffects = viewModel.container.sideEffectFlow
    )

    SignUpFlow(
        state = state,
        onIntent = viewModel::processIntent
    ) {
        NavHost(
            navController = navController,
            graph = SignUpGraphRoute(
                signUpViewModelProvider = { viewModel }
            ),
        )

        SignUpLaunchedRouterEffect(
            coroutineScope = coroutineScope,
            navController = navController,
        )
    }
}

@Composable
private fun SignUpFlow(
    state: State,
    onIntent: (Intent) -> Unit,
    content: @Composable () -> Unit,
) {
    val buttonsController = rememberSignUpButtonsController()

    AppScaffoldControllerEffect(
        appBarState = AppBarState.Custom(key = SIGN_UP_GRAPH_ROUTE_PATH) {
            AppBar(
                title = stringResource(R.string.signup),
                titleGravity = AppBarTitleGravity.CENTER,
                onGoBackClick = { onIntent(Intent.OnGoBackClick) },
            )
        }
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(vertical = 40.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))

        CompositionLocalProvider(
            value = LocalSignUpButtonsController provides buttonsController,
            content = content
        )

        Spacer(modifier = Modifier.weight(1f))

        SignUpButtons(
            state = state,
            buttonsState = buttonsController,
            onIntent = onIntent,
        )
    }
}

@Composable
private fun SignUpButtons(
    state: State,
    buttonsState: SignUpButtonsController,
    onIntent: (Intent) -> Unit,
) {
    AppButton(
        text = buttonsState.text,
        buttonState = state.registrationState.toButtonState(),
        onClick = buttonsState.onClick,
        modifier = Modifier
            .smallDeviceMaxWidth()
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    )

    if (buttonsState.isLoginButtonVisible) {
        Spacer(modifier = Modifier.height(24.dp))

        AppLinkedText(
            onClick = { onIntent(Intent.OnSignInButtonClick) },
            units = listOf(
                AppLinkedTextUnit(
                    text = stringResource(R.string.signup_already_you_member_before_annotation),
                ),
                AppLinkedTextUnit(
                    text = stringResource(R.string.signup_already_you_member_after_annotation),
                    linkTag = "sign_up",
                )
            ),
            textSpanStyle = SpanStyle(
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                color = AppTheme.colors.textBody,
            ),
            linkSpanStyle = SpanStyle(
                color = AppTheme.colors.secondary,
            ),
            isEnabled = state.registrationState == ProcessingState.None,
            modifier = Modifier
                .smallDeviceMaxWidth()
                .padding(horizontal = 24.dp)
        )
    }
}

@Composable
internal fun ColumnScope.SignUpTitle(
    @StringRes titleRes: Int
) {
    Text(
        text = stringResource(titleRes),
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Medium,
        style = AppTheme.typography.titleLarge,
        modifier = Modifier
            .padding(horizontal = 56.dp)
            .align(Alignment.CenterHorizontally)
    )
}

@Composable
private fun SignUpLaunchedRouterEffect(
    coroutineScope: CoroutineScope,
    navController: NavController,
    router: ComposeRouter = AuthComponent.signUpComposeRouter,
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
private fun SignUpScreenSideEffects(
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

internal class SignUpButtonsController {
    var text by mutableStateOf("")
    var onClick by mutableStateOf({})
    var isLoginButtonVisible by mutableStateOf(false)
}

internal val LocalSignUpButtonsController =
    staticCompositionLocalOf { SignUpButtonsController() }

@Composable
internal fun rememberSignUpButtonsController() =
    remember { SignUpButtonsController() }

@Composable
internal fun SignUpButtonsControllerEffect(
    text: String,
    isLoginButtonVisible: Boolean,
    onClick: () -> Unit,
) {
    val signUpButtonsController = LocalSignUpButtonsController.current

    LaunchedEffect(signUpButtonsController) {
        signUpButtonsController.text = text
        signUpButtonsController.isLoginButtonVisible = isLoginButtonVisible
        signUpButtonsController.onClick = onClick
    }
}

@Composable
private fun SignUpFlowPreview(
    content: @Composable (State) -> Unit,
) {
    val state = State()

    AppRootContainer { _, _ ->
        SignUpFlow(
            state = state,
            onIntent = {},
        ) {
            content(state)
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun SignUpFlowPreviewGeneralInfo() {
    SignUpFlowPreview { state ->
        SignUpGeneralInfoScreen(
            state = state,
            onIntent = {},
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun SignUpFlowPreviewPassword() {
    SignUpFlowPreview { state ->
        SignUpPasswordScreen(
            state = state,
            onIntent = {},
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun SignUpFlowPreviewEmailConfirm() {
    SignUpFlowPreview {
        SignUpEmailConfirmScreen(
            onIntent = {},
        )
    }
}
