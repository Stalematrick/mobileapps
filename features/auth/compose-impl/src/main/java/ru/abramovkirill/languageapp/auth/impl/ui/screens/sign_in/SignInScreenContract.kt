package ru.abramovkirill.languageapp.auth.impl.ui.screens.sign_in

import ru.abramovkirill.languageapp.auth.api.domain.google.AuthGoogleNonce
import ru.abramovkirill.languageapp.common.utils.StringResource
import ru.abramovkirill.languageapp.common.utils.states.ProcessingState

object SignInScreenContract {
    data class State(
        val email: String = "",
        val emailErrorMessage: StringResource? = null,
        val password: String = "",
        val passwordErrorMessage: StringResource? = null,
        val isPasswordVisible: Boolean = false,
        val authorizingState: ProcessingState = ProcessingState.None,
    )

    sealed interface Intent {
        data class OnEmailTextChanged(val text: String) : Intent
        data class OnPasswordTextChanged(val text: String) : Intent
        data class OnGoogleCredentialsReceived(
            val rawNonce: String,
            val idToken: String,
            val email: String,
            val firstName: String,
            val lastName: String,
        ) : Intent

        data object OnPasswordVisibilityToggleClick : Intent
        data object OnForgotPasswordButtonClick : Intent
        data object OnLoginButtonClick : Intent
        data object OnSignUpButtonClick : Intent
        data object OnGoogleSignInButtonClick : Intent
        data object OnGoogleCredentialsReceiveFailed : Intent
    }

    sealed interface SideEffect {
        data class Message(val text: StringResource) : SideEffect

        data class RequestGoogleCredentials(val nonce: AuthGoogleNonce) : SideEffect

        data object CloseKeyboard : SideEffect
    }
}
