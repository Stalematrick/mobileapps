package ru.abramovkirill.languageapp.auth.impl.ui.screens.recovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import ru.abramovkirill.languageapp.auth.impl.R
import ru.abramovkirill.languageapp.auth.impl.domain.recovery.InvalidRecoveryFieldsValuesException
import ru.abramovkirill.languageapp.auth.impl.domain.recovery.RecoveryUseCase
import ru.abramovkirill.languageapp.auth.impl.ui.navigation.recovery.RECOVERY_CHECK_EMAIL_SCREEN_ROUTE_PATH
import ru.abramovkirill.languageapp.auth.impl.ui.navigation.recovery.RECOVERY_CHOOSE_PASSWORD_SCREEN_ROUTE_PATH
import ru.abramovkirill.languageapp.auth.impl.ui.navigation.recovery.RECOVERY_SUCCESS_SCREEN_ROUTE_PATH
import ru.abramovkirill.languageapp.auth.impl.ui.navigation.sign_in.SIGN_IN_SCREEN_ROUTE_PATH
import ru.abramovkirill.languageapp.auth.impl.ui.screens.recovery.RecoveryFlowContract.Intent
import ru.abramovkirill.languageapp.auth.impl.ui.screens.recovery.RecoveryFlowContract.SideEffect
import ru.abramovkirill.languageapp.auth.impl.ui.screens.recovery.RecoveryFlowContract.State
import ru.abramovkirill.languageapp.common.utils.launchSafe
import ru.abramovkirill.languageapp.common.utils.states.ProcessingState
import ru.abramovkirill.languageapp.common.utils.strRes
import ru.abramovkirill.languageapp.core.design.utils.withReturnToNone
import ru.abramovkirill.languageapp.core.navigation.api.Router
import ru.abramovkirill.languageapp.core.navigation.api.RoutingOption
import ru.abramovkirill.languageapp.core.design.R as DesignR

private typealias IntentBody = SimpleSyntax<State, SideEffect>

class RecoveryFlowViewModel(
    private val resetPassword: RecoveryUseCase,
    private val router: Router,
    private val recoveryRouter: Router,
) : ViewModel(), ContainerHost<State, SideEffect> {
    override val container = container<State, SideEffect>(
        initialState = State()
    )

    fun processIntent(intent: Intent) = intent {
        when (intent) {
            is Intent.OnEmailChanged ->
                onEmailChanged(intent.text)
            is Intent.OnPasswordChanged ->
                onPasswordChanged(intent.text)
            is Intent.OnConfirmedPasswordChanged ->
                onConfirmedPasswordChanged(intent.text)
            Intent.OnPasswordVisibilityToggleClick ->
                onPasswordVisibilityToggleClick()
            Intent.OnConfirmedPasswordVisibilityToggleClick ->
                onConfirmedPasswordVisibilityToggleClick()
            Intent.OnNewPasswordConfirmButtonClick ->
                onConfirmNewPasswordButtonClick()
            Intent.OnGoBackClick ->
                onGoBackClick()
            Intent.OnResetPasswordButtonClick ->
                onResetPasswordButtonClick()
            Intent.OnCheckEmailOkButtonClick ->
                onCheckEmailOkButtonClick()
            Intent.OnFinishButtonClick ->
                onFinishButtonClick()
        }

        processKeyboardClose(intent)
    }

    private suspend fun IntentBody.processKeyboardClose(intent: Intent) {
        when (intent) {
            is Intent.OnCheckEmailOkButtonClick,
            Intent.OnResetPasswordButtonClick,
            Intent.OnGoBackClick,
            Intent.OnNewPasswordConfirmButtonClick,-> {
                postSideEffect(SideEffect.CloseKeyboard)
            }
            else -> Unit
        }
    }

    private suspend fun IntentBody.onEmailChanged(text: String) {
        reduce {
            state.copy(
                email = text,
                emailErrorMessage = null
            )
        }
    }

    private suspend fun IntentBody.onPasswordChanged(text: String) {
        reduce {
            state.copy(
                password = text,
                passwordErrorMessage = null
            )
        }
    }

    private suspend fun IntentBody.onConfirmedPasswordChanged(text: String) {
        reduce {
            state.copy(
                confirmedPassword = text,
                confirmedPasswordErrorMessage = null
            )
        }
    }

    private suspend fun IntentBody.onPasswordVisibilityToggleClick() {
        reduce { state.copy(isPasswordVisible = !state.isPasswordVisible) }
    }

    private suspend fun IntentBody.onConfirmedPasswordVisibilityToggleClick() {
        reduce { state.copy(isConfirmedPasswordVisible = !state.isConfirmedPasswordVisible) }
    }

    private suspend fun onGoBackClick() {
        when {
            recoveryRouter.previousRoute == null -> router.navigateBack()
            else -> recoveryRouter.navigateBack()
        }
    }

    private suspend fun IntentBody.onResetPasswordButtonClick() {
        viewModelScope.launchSafe(
            block = {
                reduce { state.copy(recoveringState = ProcessingState.InProgress) }

                resetPassword(email = state.email)

                reduce { state.copy(recoveringState = ProcessingState.None) }

                recoveryRouter.navigate(RECOVERY_CHECK_EMAIL_SCREEN_ROUTE_PATH)
            },
            onError = { throwable ->
                withReturnToNone(startWith = ProcessingState.Error) { recoveringState ->
                    reduce { state.copy(recoveringState = recoveringState) }
                }

                when (throwable) {
                    is InvalidRecoveryFieldsValuesException -> {
                        reduceError(throwable)
                    }

                    else -> {
                        postSideEffect(SideEffect.Message(strRes(DesignR.string.error_smth_went_wrong)))
                    }
                }
            }
        )
    }

    private suspend fun onCheckEmailOkButtonClick() {
        router.navigate(
            routePath = SIGN_IN_SCREEN_ROUTE_PATH,
            options = listOf(
                RoutingOption.PopUpTo(SIGN_IN_SCREEN_ROUTE_PATH),
                RoutingOption.LaunchSingleTop(true),
            )
        )
    }

    private suspend fun IntentBody.onConfirmNewPasswordButtonClick() {
        viewModelScope.launchSafe(
            block = {
                reduce { state.copy(recoveringState = ProcessingState.InProgress) }

                resetPassword.changePassword(
                    password = state.password,
                    confirmedPassword = state.confirmedPassword,
                )

                withReturnToNone(startWith = ProcessingState.Success) { recoveringState ->
                    reduce { state.copy(recoveringState = recoveringState) }
                }

                recoveryRouter.navigate(
                    routePath = RECOVERY_SUCCESS_SCREEN_ROUTE_PATH,
                    options = listOf(
                        RoutingOption.PopUpTo(
                            routePath = RECOVERY_CHOOSE_PASSWORD_SCREEN_ROUTE_PATH,
                            inclusive = true,
                        )
                    )
                )
            },
            onError = { throwable ->
                withReturnToNone(startWith = ProcessingState.Error) { recoveringState ->
                    reduce { state.copy(recoveringState = recoveringState) }
                }

                when (throwable) {
                    is InvalidRecoveryFieldsValuesException -> {
                        reduceError(throwable)
                    }

                    else -> {
                        postSideEffect(SideEffect.Message(strRes(DesignR.string.error_smth_went_wrong)))
                    }
                }
            }
        )
    }

    private suspend fun onFinishButtonClick() {
        router.navigate(
            routePath = SIGN_IN_SCREEN_ROUTE_PATH,
            options = listOf(
                RoutingOption.PopUpTo(SIGN_IN_SCREEN_ROUTE_PATH),
                RoutingOption.LaunchSingleTop(true),
            )
        )
    }

    private suspend fun IntentBody.reduceError(
        throwable: InvalidRecoveryFieldsValuesException
    ) {
        reduce {
            state.copy(
                emailErrorMessage = throwable.emailError.toStringResource(),
                passwordErrorMessage = throwable.passwordError.toStringResource(),
                confirmedPasswordErrorMessage = throwable.confirmedPasswordError.toStringResource(),
            )
        }
    }

    private fun InvalidRecoveryFieldsValuesException.Email?.toStringResource() = when (this) {
        InvalidRecoveryFieldsValuesException.Email.EMPTY -> strRes(R.string.error_empty_field)
        null -> null
    }

    private fun InvalidRecoveryFieldsValuesException.Password?.toStringResource() = when (this) {
        InvalidRecoveryFieldsValuesException.Password.MIN_LENGTH -> strRes(R.string.error_password_to_short)
        null -> null
    }

    private fun InvalidRecoveryFieldsValuesException.ConfirmedPassword?.toStringResource() = when (this) {
        InvalidRecoveryFieldsValuesException.ConfirmedPassword.MISMATCH -> strRes(R.string.error_password_mismatch)
        null -> null
    }
}
