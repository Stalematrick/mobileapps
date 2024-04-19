package ru.abramovkirill.languageapp.auth.impl.ui.composables.choose_password

import ru.abramovkirill.languageapp.common.utils.StringResource

object ChoosePasswordContract {
    data class State(
        val password: String = "",
        val passwordErrorMessage: StringResource? = null,
        val confirmedPassword: String = "",
        val confirmedPasswordErrorMessage: StringResource? = null,
        val isPasswordVisible: Boolean = false,
        val isConfirmedPasswordVisible: Boolean = false,
        val isEnabled: Boolean = true,
    )

    sealed interface Intent {
        data object OnKeyboardDoneAction : Intent
        data object OnPasswordVisibilityToggleClick : Intent
        data object OnConfirmedPasswordVisibilityToggleClick : Intent
        data class OnPasswordChanged(val text: String) : Intent
        data class OnConfirmedPasswordChanged(val text: String) : Intent
    }
}
