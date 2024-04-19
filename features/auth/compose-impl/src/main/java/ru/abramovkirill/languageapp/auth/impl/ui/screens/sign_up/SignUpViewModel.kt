package ru.abramovkirill.languageapp.auth.impl.ui.screens.sign_up

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
import ru.abramovkirill.languageapp.auth.impl.domain.sign_up.InvalidSignUpFieldsValuesException
import ru.abramovkirill.languageapp.auth.impl.domain.sign_up.SignUpUseCase
import ru.abramovkirill.languageapp.auth.impl.ui.navigation.sign_in.SIGN_IN_SCREEN_ROUTE_PATH
import ru.abramovkirill.languageapp.auth.impl.ui.navigation.sign_up.SIGN_UP_EMAIL_CONFIRM_SCREEN_ROUTE_PATH
import ru.abramovkirill.languageapp.auth.impl.ui.navigation.sign_up.SIGN_UP_PASSWORD_SCREEN_ROUTE_PATH
import ru.abramovkirill.languageapp.auth.impl.ui.screens.sign_up.SignUpScreenContract.Intent
import ru.abramovkirill.languageapp.auth.impl.ui.screens.sign_up.SignUpScreenContract.SideEffect
import ru.abramovkirill.languageapp.auth.impl.ui.screens.sign_up.SignUpScreenContract.State
import ru.abramovkirill.languageapp.common.utils.launchSafe
import ru.abramovkirill.languageapp.common.utils.states.ProcessingState
import ru.abramovkirill.languageapp.common.utils.strRes
import ru.abramovkirill.languageapp.core.design.utils.withReturnToNone
import ru.abramovkirill.languageapp.core.navigation.api.Router
import ru.abramovkirill.languageapp.core.navigation.api.RoutingOption
import ru.abramovkirill.languageapp.core.design.R as DesignR

private typealias IntentBody = SimpleSyntax<State, SideEffect>

class SignUpViewModel(
    private val router: Router,
    private val signUpRouter: Router,
    private val signUp: SignUpUseCase,
) : ViewModel(), ContainerHost<State, SideEffect> {
    override val container = container<State, SideEffect>(
        initialState = State()
    )

    fun processIntent(intent: Intent) = intent {
        when (intent) {
            is Intent.OnEmailChanged ->
                onEmailChanged(intent.text)
            is Intent.OnFirstNameChanged ->
                onFirstNameChanged(intent.text)
            is Intent.OnLastNameChanged ->
                onLastNameChanged(intent.text)
            is Intent.OnPasswordChanged ->
                onPasswordChanged(intent.text)
            is Intent.OnConfirmedPasswordChanged ->
                onConfirmedPasswordChanged(intent.text)
            Intent.OnContinueButtonClick ->
                onContinueButtonClick()
            Intent.OnGoBackClick ->
                onGoBackClick()
            Intent.OnPasswordVisibilityToggleClick ->
                onPasswordVisibilityToggleClick()
            Intent.OnConfirmedPasswordVisibilityToggleClick ->
                onConfirmedPasswordVisibilityToggleClick()
            Intent.OnSignInButtonClick ->
                onSignInButtonClick()
            Intent.OnSignUpButtonClick ->
                onSignUpButtonClick()
            Intent.OnFinishButtonClick ->
                onFinishButtonClick()
        }
    }

    private suspend fun IntentBody.onFirstNameChanged(text: String) {
        reduce {
            state.copy(
                firstName = text,
                firstNameErrorMessage = null,
            )
        }
    }

    private suspend fun IntentBody.onLastNameChanged(text: String) {
        reduce {
            state.copy(
                lastName = text,
                lastNameErrorMessage = null,
            )
        }
    }

    private suspend fun IntentBody.onEmailChanged(text: String) {
        reduce {
            state.copy(
                email = text,
                emailErrorMessage = null,
            )
        }
    }

    private suspend fun IntentBody.onPasswordChanged(text: String) {
        reduce {
            state.copy(
                password = text,
                passwordErrorMessage = null,
            )
        }
    }

    private suspend fun IntentBody.onConfirmedPasswordChanged(text: String) {
        reduce {
            state.copy(
                confirmedPassword = text,
                confirmedPasswordErrorMessage = null,
            )
        }
    }

    private suspend fun onGoBackClick() {
        when {
            signUpRouter.currentRoute == SIGN_UP_EMAIL_CONFIRM_SCREEN_ROUTE_PATH ->
                router.navigateBack()
            signUpRouter.previousRoute == null ->
                router.navigateBack()
            else ->
                signUpRouter.navigateBack()
        }
    }

    private suspend fun IntentBody.onPasswordVisibilityToggleClick() {
        reduce {
            state.copy(
                isPasswordVisible = !state.isPasswordVisible
            )
        }
    }

    private suspend fun IntentBody.onConfirmedPasswordVisibilityToggleClick() {
        reduce {
            state.copy(
                isConfirmedPasswordVisible = !state.isConfirmedPasswordVisible
            )
        }
    }

    private suspend fun onSignInButtonClick() {
        navigateToSignIn()
    }

    private suspend fun IntentBody.onContinueButtonClick() {
        viewModelScope.launchSafe(
            block = {
                signUp.validate(
                    firstName = state.firstName,
                    lastName = state.lastName,
                    email = state.email,
                    fieldsToValidate = listOf(
                        SignUpUseCase.Field.FIRST_NAME,
                        SignUpUseCase.Field.LAST_NAME,
                        SignUpUseCase.Field.EMAIL,
                    ),
                )

                signUpRouter.navigate(routePath = SIGN_UP_PASSWORD_SCREEN_ROUTE_PATH)
            },
            onError = { throwable ->
                withReturnToNone(startWith = ProcessingState.Error) { registrationState ->
                    reduce { state.copy(registrationState = registrationState) }
                }

                when (throwable) {
                    is InvalidSignUpFieldsValuesException -> {
                        reduceErrors(throwable)
                    }
                    else -> {
                        postSideEffect(SideEffect.Message(strRes(DesignR.string.error_smth_went_wrong)))
                    }
                }
            }
        )
    }

    private suspend fun IntentBody.onSignUpButtonClick() {
        viewModelScope.launchSafe(
            block = {
                reduce {
                    state.copy(registrationState = ProcessingState.InProgress)
                }

                signUp(
                    firstName = state.firstName,
                    lastName = state.lastName,
                    email = state.email,
                    password = state.password,
                    confirmedPassword = state.confirmedPassword,
                )

                withReturnToNone(startWith = ProcessingState.Success) { registrationState ->
                    reduce {
                        state.copy(registrationState = registrationState)
                    }
                }

                signUpRouter.navigate(routePath = SIGN_UP_EMAIL_CONFIRM_SCREEN_ROUTE_PATH)
            },
            onError = { throwable ->
                withReturnToNone(startWith = ProcessingState.Error) { registrationState ->
                    reduce { state.copy(registrationState = registrationState) }
                }

                when (throwable) {
                    is InvalidSignUpFieldsValuesException -> {
                        reduceErrors(throwable)
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

    private suspend fun onFinishButtonClick() {
        navigateToSignIn()
    }

    private suspend fun IntentBody.reduceErrors(
        throwable: InvalidSignUpFieldsValuesException
    ) {
        reduce {
            state.copy(
                firstNameErrorMessage = throwable.firstNameError.toStringResource(),
                lastNameErrorMessage = throwable.lastNameError.toStringResource(),
                emailErrorMessage = throwable.emailError.toStringResource(),
                passwordErrorMessage = throwable.passwordError.toStringResource(),
                confirmedPasswordErrorMessage = throwable.confirmedPasswordError.toStringResource(),
            )
        }
    }

    private suspend fun navigateToSignIn() {
        router.navigate(
            routePath = SIGN_IN_SCREEN_ROUTE_PATH,
            options = listOf(
                RoutingOption.PopUpTo(SIGN_IN_SCREEN_ROUTE_PATH),
                RoutingOption.LaunchSingleTop(true),
            )
        )
    }

    private fun InvalidSignUpFieldsValuesException.Name?.toStringResource() = when (this) {
        InvalidSignUpFieldsValuesException.Name.EMPTY -> strRes(R.string.error_empty_field)
        null -> null
    }

    private fun InvalidSignUpFieldsValuesException.Email?.toStringResource() = when (this) {
        InvalidSignUpFieldsValuesException.Email.INVALID_FORMAT -> strRes(R.string.error_invalid_email)
        null -> null
    }

    private fun InvalidSignUpFieldsValuesException.Password?.toStringResource() = when (this) {
        InvalidSignUpFieldsValuesException.Password.MIN_LENGTH -> strRes(R.string.error_password_to_short)
        null -> null
    }

    private fun InvalidSignUpFieldsValuesException.ConfirmedPassword?.toStringResource() = when (this) {
        InvalidSignUpFieldsValuesException.ConfirmedPassword.MISMATCH -> strRes(R.string.error_password_mismatch)
        null -> null
    }
}
