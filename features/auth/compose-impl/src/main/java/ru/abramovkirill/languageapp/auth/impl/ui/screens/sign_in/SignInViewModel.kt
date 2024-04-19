package ru.abramovkirill.languageapp.auth.impl.ui.screens.sign_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.exceptions.BadRequestRestException
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import ru.abramovkirill.languageapp.auth.impl.R
import ru.abramovkirill.languageapp.auth.api.domain.google.AuthGoogleNonceProvider
import ru.abramovkirill.languageapp.auth.impl.domain.sign_in.InvalidSignInFieldsValuesException
import ru.abramovkirill.languageapp.auth.impl.domain.sign_in.SignInUseCase
import ru.abramovkirill.languageapp.auth.impl.domain.sign_in.SignInWithGoogleUseCase
import ru.abramovkirill.languageapp.auth.impl.ui.navigation.recovery.RECOVERY_FLOW_ROUTE_PATH
import ru.abramovkirill.languageapp.auth.impl.ui.navigation.sign_in.SIGN_IN_SCREEN_ROUTE_PATH
import ru.abramovkirill.languageapp.auth.impl.ui.navigation.sign_up.SIGN_UP_FLOW_ROUTE_PATH
import ru.abramovkirill.languageapp.auth.impl.ui.screens.sign_in.SignInScreenContract.Intent
import ru.abramovkirill.languageapp.auth.impl.ui.screens.sign_in.SignInScreenContract.SideEffect
import ru.abramovkirill.languageapp.auth.impl.ui.screens.sign_in.SignInScreenContract.State
import ru.abramovkirill.languageapp.common.utils.launchSafe
import ru.abramovkirill.languageapp.common.utils.states.ProcessingState
import ru.abramovkirill.languageapp.common.utils.strRes
import ru.abramovkirill.languageapp.core.design.utils.withReturnToNone
import ru.abramovkirill.languageapp.core.navigation.api.Router
import ru.abramovkirill.languageapp.core.navigation.api.RoutingOption
import ru.abramovkirill.languageapp.main.api.ui.navigation.MAIN_SCREEN_ROUTE_PATH
import ru.abramovkirill.languageapp.core.design.R as DesignR

typealias IntentBody = SimpleSyntax<State, SideEffect>

class SignInViewModel(
    private val signIn: SignInUseCase,
    private val signInWithGoogle: SignInWithGoogleUseCase,
    private val authGoogleNonceProvider: AuthGoogleNonceProvider,
    private val router: Router,
) : ViewModel(), ContainerHost<State, SideEffect> {
    override val container = container<State, SideEffect>(
        initialState = State()
    )

    fun processIntent(intent: Intent) = intent {
        when (intent) {
            is Intent.OnEmailTextChanged ->
                onEmailTextChanged(intent.text)
            is Intent.OnPasswordTextChanged ->
                onPasswordTextChanged(intent.text)
            is Intent.OnGoogleCredentialsReceived ->
                onGoogleCredentialsReceived(
                    rawNonce = intent.rawNonce,
                    idToken = intent.idToken,
                    email = intent.email,
                    firstName = intent.firstName,
                    lastName = intent.lastName
                )
            Intent.OnForgotPasswordButtonClick ->
                onForgotPasswordButtonClick()
            Intent.OnGoogleSignInButtonClick ->
                onGoogleSignInButtonClick()
            Intent.OnLoginButtonClick ->
                onLoginButtonClick()
            Intent.OnPasswordVisibilityToggleClick ->
                onPasswordVisibilityToggleClick()
            Intent.OnSignUpButtonClick ->
                onSignUpButtonClick()
            Intent.OnGoogleCredentialsReceiveFailed ->
                onGoogleCredentialsReceiveFailed()
        }

        processKeyboardClose(intent)
    }

    private suspend fun IntentBody.processKeyboardClose(intent: Intent) {
        when (intent) {
            is Intent.OnForgotPasswordButtonClick,
            Intent.OnGoogleSignInButtonClick,
            Intent.OnLoginButtonClick,
            Intent.OnSignUpButtonClick -> {
                postSideEffect(SideEffect.CloseKeyboard)
            }
            else -> Unit
        }
    }

    private suspend fun IntentBody.onEmailTextChanged(text: String) {
        reduce {
            state.copy(
                email = text,
                emailErrorMessage = null,
            )
        }
    }

    private suspend fun IntentBody.onPasswordTextChanged(text: String) {
        reduce {
            state.copy(
                password = text,
                passwordErrorMessage = null,
            )
        }
    }

    private suspend fun onForgotPasswordButtonClick() {
        router.navigate(routePath = RECOVERY_FLOW_ROUTE_PATH)
    }

    private suspend fun IntentBody.onGoogleSignInButtonClick() {
        postSideEffect(SideEffect.RequestGoogleCredentials(
            nonce = authGoogleNonceProvider.provideNonce()
        ))
    }

    private suspend fun IntentBody.onGoogleCredentialsReceived(
        rawNonce: String,
        idToken: String,
        email: String,
        firstName: String,
        lastName: String,
    ) {
        viewModelScope.launchSafe(
            block = {
                reduce { state.copy(authorizingState = ProcessingState.InProgress) }

                signInWithGoogle(
                    rawNonce = rawNonce,
                    idToken = idToken,
                    email = email,
                    firstName = firstName,
                    lastName = lastName,
                )

                withReturnToNone(startWith = ProcessingState.Success) { recoveringState ->
                    reduce { state.copy(authorizingState = recoveringState) }
                }

                router.navigate(
                    routePath = MAIN_SCREEN_ROUTE_PATH,
                    options = listOf(
                        RoutingOption.PopUpTo(
                            routePath = SIGN_IN_SCREEN_ROUTE_PATH,
                            inclusive = true,
                        )
                    )
                )
            },
            onError = {
                postSideEffect(SideEffect.Message(strRes(DesignR.string.error_smth_went_wrong)))
            }
        )
    }

    private suspend fun IntentBody.onGoogleCredentialsReceiveFailed() {
        postSideEffect(SideEffect.Message(strRes(DesignR.string.error_smth_went_wrong)))
    }

    private suspend fun IntentBody.onLoginButtonClick() {
        viewModelScope.launchSafe(
            block = {
                reduce { state.copy(authorizingState = ProcessingState.InProgress) }

//                signIn(
//                    email = state.email,
//                    password = state.password
//                )

                withReturnToNone(startWith = ProcessingState.Success) { recoveringState ->
                    reduce { state.copy(authorizingState = recoveringState) }
                }

                router.navigate(
                    routePath = MAIN_SCREEN_ROUTE_PATH,
                    options = listOf(
                        RoutingOption.PopUpTo(
                            routePath = SIGN_IN_SCREEN_ROUTE_PATH,
                            inclusive = true,
                        )
                    )
                )
            },
            onError = { throwable ->
                withReturnToNone(startWith = ProcessingState.Error) { recoveringState ->
                    reduce { state.copy(authorizingState = recoveringState) }
                }

                when (throwable) {
                    is InvalidSignInFieldsValuesException -> {
                        reduce {
                            state.copy(
                                emailErrorMessage = throwable.emailError.toStringResource(),
                                passwordErrorMessage = throwable.passwordError.toStringResource(),
                            )
                        }
                    }

                    is BadRequestRestException -> {
                        postSideEffect(SideEffect.Message(strRes(R.string.error_check_your_creds)))
                    }

                    else -> {
                        postSideEffect(SideEffect.Message(strRes(DesignR.string.error_smth_went_wrong)))
                    }
                }
            }
        )
    }

    private suspend fun IntentBody.onSignUpButtonClick() {
        router.navigate(SIGN_UP_FLOW_ROUTE_PATH)
    }

    private suspend fun IntentBody.onPasswordVisibilityToggleClick() {
        reduce {
            state.copy(
                isPasswordVisible = !state.isPasswordVisible
            )
        }
    }

    private fun InvalidSignInFieldsValuesException.Email?.toStringResource() = when (this) {
        InvalidSignInFieldsValuesException.Email.EMPTY -> strRes(R.string.error_empty_field)
        null -> null
    }

    private fun InvalidSignInFieldsValuesException.Password?.toStringResource() = when (this) {
        InvalidSignInFieldsValuesException.Password.EMPTY -> strRes(R.string.error_empty_field)
        null -> null
    }
}
