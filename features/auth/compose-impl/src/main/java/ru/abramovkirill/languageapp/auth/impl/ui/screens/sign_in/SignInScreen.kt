package ru.abramovkirill.languageapp.auth.impl.ui.screens.sign_in

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.flow.Flow
import ru.abramovkirill.languageapp.auth.impl.R
import ru.abramovkirill.languageapp.auth.api.domain.google.AuthGoogleNonce
import ru.abramovkirill.languageapp.auth.impl.di.AuthComponent
import ru.abramovkirill.languageapp.auth.impl.ui.navigation.sign_in.SIGN_IN_SCREEN_ROUTE_PATH
import ru.abramovkirill.languageapp.auth.impl.ui.screens.sign_in.SignInScreenContract.Intent
import ru.abramovkirill.languageapp.auth.impl.ui.screens.sign_in.SignInScreenContract.SideEffect
import ru.abramovkirill.languageapp.auth.impl.ui.screens.sign_in.SignInScreenContract.State
import ru.abramovkirill.languageapp.auth.impl.ui.utils.isFatal
import ru.abramovkirill.languageapp.common.utils.compose.ScreenSideEffectsFlowHandler
import ru.abramovkirill.languageapp.common.utils.compose.handleMessageSideEffect
import ru.abramovkirill.languageapp.common.utils.extract
import ru.abramovkirill.languageapp.common.utils.states.ProcessingState
import ru.abramovkirill.languageapp.core.design.composables.AppBar
import ru.abramovkirill.languageapp.core.design.composables.AppBarTitleGravity
import ru.abramovkirill.languageapp.core.design.composables.AppLinkedText
import ru.abramovkirill.languageapp.core.design.composables.AppLinkedTextUnit
import ru.abramovkirill.languageapp.core.design.composables.AppRootContainer
import ru.abramovkirill.languageapp.core.design.composables.button.AppButton
import ru.abramovkirill.languageapp.core.design.composables.button.AppButtonState
import ru.abramovkirill.languageapp.core.design.composables.button.AppTextButton
import ru.abramovkirill.languageapp.core.design.composables.button.toButtonState
import ru.abramovkirill.languageapp.core.design.composables.scaffold.AppBarState
import ru.abramovkirill.languageapp.core.design.composables.scaffold.AppScaffoldControllerEffect
import ru.abramovkirill.languageapp.core.design.composables.scaffold.LocalAppScaffoldController
import ru.abramovkirill.languageapp.core.design.composables.text_field.AppPasswordTextField
import ru.abramovkirill.languageapp.core.design.composables.text_field.AppTextField
import ru.abramovkirill.languageapp.core.design.styles.AppTheme
import ru.abramovkirill.languageapp.core.design.utils.COMPOSE_LARGE_DEVICE_SPEC
import ru.abramovkirill.languageapp.core.design.utils.smallDeviceMaxWidth

@Composable
fun SignInScreen(viewModel: SignInViewModel) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val sideEffects = viewModel.container.sideEffectFlow

    SignInScreenSideEffects(
        sideEffects = sideEffects,
        onIntent = viewModel::processIntent
    )

    SignInScreen(
        state = state,
        onIntent = viewModel::processIntent
    )
}

@Composable
private fun SignInScreen(
    state: State,
    onIntent: (Intent) -> Unit,
) {
    val appBarTitle = stringResource(R.string.login)

    AppScaffoldControllerEffect(
        appBarState = AppBarState.Custom(key = SIGN_IN_SCREEN_ROUTE_PATH) {
            AppBar(
                title = appBarTitle,
                titleGravity = AppBarTitleGravity.CENTER,
            )
        }
    )

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(24.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.sign_in_learn_at_home_logo),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.sign_in_logo_title),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            style = AppTheme.typography.titleLarge,
            modifier = Modifier
                .padding(horizontal = 56.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(32.dp))

        SignInEmailPasswordBlock(
            email = state.email,
            password = state.password,
            isInputEnabled = state.authorizingState == ProcessingState.None,
            emailErrorMessage = state.emailErrorMessage?.extract(),
            passwordErrorMessage = state.passwordErrorMessage?.extract(),
            isPasswordVisible = state.isPasswordVisible,
            onIntent = onIntent,
        )

        Spacer(modifier = Modifier.height(32.dp))

        SignInButtonsBlock(
            loginButtonState = state.authorizingState.toButtonState(),
            isLinkedTextEnabled = state.authorizingState == ProcessingState.None,
            onIntent = onIntent,
        )
    }
}

@Composable
private fun SignInEmailPasswordBlock(
    email: String,
    password: String,
    isPasswordVisible: Boolean,
    isInputEnabled: Boolean,
    emailErrorMessage: String? = null,
    passwordErrorMessage: String? = null,
    onIntent: (Intent) -> Unit,
) {
    Column(
        modifier = Modifier
            .smallDeviceMaxWidth()
    ) {
        AppTextField(
            value = email,
            isEnabled = isInputEnabled,
            placeholder = stringResource(R.string.email),
            label = stringResource(R.string.email_address),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email,
            ),
            errorMessage = emailErrorMessage,
            onValueChange = { onIntent(Intent.OnEmailTextChanged(it)) },
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        AppPasswordTextField(
            value = password,
            isEnabled = isInputEnabled,
            isPasswordVisible = isPasswordVisible,
            label = stringResource(R.string.password),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { onIntent(Intent.OnLoginButtonClick) }
            ),
            errorMessage = passwordErrorMessage,
            onValueChange = { onIntent(Intent.OnPasswordTextChanged(it)) },
            onPasswordVisibilityToggleClick = { onIntent(Intent.OnPasswordVisibilityToggleClick) },
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(4.dp))

        AppTextButton(
            text = stringResource(R.string.sign_in_forgot_password),
            isEnabled = isInputEnabled,
            textColor = AppTheme.colors.error,
            contentPadding = PaddingValues(0.dp),
            onClick = { onIntent(Intent.OnForgotPasswordButtonClick) },
            modifier = Modifier
                .wrapContentWidth()
        )
    }
}

@Composable
private fun ColumnScope.SignInButtonsBlock(
    loginButtonState: AppButtonState,
    isLinkedTextEnabled: Boolean,
    onIntent: (Intent) -> Unit,
) {
    AppButton(
        text = stringResource(R.string.login),
        buttonState = loginButtonState,
        onClick = { onIntent(Intent.OnLoginButtonClick) },
        modifier = Modifier
            .smallDeviceMaxWidth()
            .fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(24.dp))

    LoginLinkedText(
        onClick = { onIntent(Intent.OnSignUpButtonClick) },
        units = listOf(
            AppLinkedTextUnit(
                text = stringResource(R.string.sign_in_not_you_member_before_annotation),
            ),
            AppLinkedTextUnit(
                text = stringResource(R.string.sign_in_not_you_member_annotation),
                linkTag = "Signup"
            )
        ),
        isEnabled = isLinkedTextEnabled,
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
    )

    Spacer(modifier = Modifier.height(12.dp))

    LoginLinkedText(
        onClick = { onIntent(Intent.OnGoogleSignInButtonClick) },
        units = listOf(
            AppLinkedTextUnit(
                text = stringResource(R.string.sign_in_use_google_before_annotation),
            ),
            AppLinkedTextUnit(
                text = stringResource(R.string.sign_in_use_google_annotation),
                linkTag = "Signup"
            ),
            AppLinkedTextUnit(
                text = stringResource(R.string.sign_in_use_google_after_annotation),
            )
        ),
        isEnabled = isLinkedTextEnabled,
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
    )
}

@Composable
private fun LoginLinkedText(
    onClick: () -> Unit,
    isEnabled: Boolean,
    units: List<AppLinkedTextUnit>,
    modifier: Modifier = Modifier,
) {
    AppLinkedText(
        onClick = { onClick() },
        units = units,
        textSpanStyle = SpanStyle(
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium,
            color = AppTheme.colors.textBody,
        ),
        linkSpanStyle = SpanStyle(
            color = AppTheme.colors.secondary,
        ),
        isEnabled = isEnabled,
        modifier = modifier
    )
}

@Composable
private fun SignInScreenSideEffects(
    sideEffects: Flow<SideEffect>,
    onIntent: (Intent) -> Unit,
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarHostState = LocalAppScaffoldController.current.snackbarHostState

    val credentialManager = remember(context.applicationContext) {
        CredentialManager.create(context.applicationContext)
    }

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

            is SideEffect.RequestGoogleCredentials -> {
                requestGoogleCredentials(
                    context = context,
                    credentialManager = credentialManager,
                    nonce = sideEffect.nonce,
                    onIntent = onIntent,
                )
            }
        }
    }
}

private suspend fun requestGoogleCredentials(
    context: Context,
    nonce: AuthGoogleNonce,
    credentialManager: CredentialManager,
    onIntent: (Intent) -> Unit,
) {
    try {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(AuthComponent.environmentConfig.googleServerClientId)
            .setNonce(nonce.encoded)
            .build()

        val credentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val credentialRequestResponse = credentialManager.getCredential(
            context = context,
            request = credentialRequest
        )

        val googleIdTokenCredential = GoogleIdTokenCredential
            .createFrom(credentialRequestResponse.credential.data)

        onIntent(Intent.OnGoogleCredentialsReceived(
            rawNonce = nonce.raw,
            idToken = googleIdTokenCredential.idToken,
            email = googleIdTokenCredential.id,
            firstName = googleIdTokenCredential.givenName ?: "",
            lastName = googleIdTokenCredential.familyName ?: "",
        ))
    } catch (e: GetCredentialException) {
        if (e.isFatal()) {
            onIntent(Intent.OnGoogleCredentialsReceiveFailed)
        }
    }
}

@Composable
private fun LoginScreenPreview() {
    AppRootContainer { _, _ ->
        SignInScreen(
            state = State(),
            onIntent = {}
        )
    }
}

@Composable
@Preview(
    showBackground = true
)
private fun LoginScreenPreviewLight() {
    LoginScreenPreview()
}

@Composable
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
private fun LoginScreenPreviewNight() {
    LoginScreenPreview()
}

@Composable
@Preview(
    showBackground = true,
    device = COMPOSE_LARGE_DEVICE_SPEC,
)
private fun LoginScreenPreviewLightLarge() {
    LoginScreenPreview()
}

@Composable
@Preview(
    showBackground = true,
    device = COMPOSE_LARGE_DEVICE_SPEC,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
private fun LoginScreenPreviewNightLarge() {
    LoginScreenPreview()
}
