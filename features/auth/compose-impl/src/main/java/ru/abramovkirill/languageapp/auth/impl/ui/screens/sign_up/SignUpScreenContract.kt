package ru.abramovkirill.languageapp.auth.impl.ui.screens.sign_up

import ru.abramovkirill.languageapp.common.utils.StringResource
import ru.abramovkirill.languageapp.common.utils.states.ProcessingState

object SignUpScreenContract {
    data class State(
        val firstName: String = "",
        val firstNameErrorMessage: StringResource? = null,
        val lastName: String = "",
        val lastNameErrorMessage: StringResource? = null,
        val email: String = "",
        val emailErrorMessage: StringResource? = null,
        val password: String = "",
        val passwordErrorMessage: StringResource? = null,
        val confirmedPassword: String = "",
        val confirmedPasswordErrorMessage: StringResource? = null,
        val isPasswordVisible: Boolean = false,
        val isConfirmedPasswordVisible: Boolean = false,
        val registrationState: ProcessingState = ProcessingState.None,
    )

    sealed interface Intent {
        data class OnFirstNameChanged(val text: String) : Intent
        data class OnLastNameChanged(val text: String) : Intent
        data class OnEmailChanged(val text: String) : Intent
        data class OnPasswordChanged(val text: String) : Intent
        data class OnConfirmedPasswordChanged(val text: String) : Intent

        data object OnPasswordVisibilityToggleClick : Intent
        data object OnConfirmedPasswordVisibilityToggleClick : Intent
        data object OnSignInButtonClick : Intent
        data object OnSignUpButtonClick : Intent
        data object OnContinueButtonClick : Intent
        data object OnFinishButtonClick : Intent
        data object OnGoBackClick : Intent
    }

    sealed interface SideEffect {
        data class Message(val text: StringResource) : SideEffect
        data object CloseKeyboard : SideEffect
    }
}
