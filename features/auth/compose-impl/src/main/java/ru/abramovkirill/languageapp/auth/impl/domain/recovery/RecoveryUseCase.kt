package ru.abramovkirill.languageapp.auth.impl.domain.recovery

import ru.abramovkirill.languageapp.auth.api.domain.AuthRepository
import ru.abramovkirill.languageapp.auth.impl.domain.password_validator.InvalidPasswordValuesException
import ru.abramovkirill.languageapp.auth.impl.domain.password_validator.PasswordValidator

class RecoveryUseCase(
    private val repository: AuthRepository,
    private val validatePassword: PasswordValidator
) {
    suspend operator fun invoke(email: String) {
        if (email.isBlank()) {
            throw InvalidRecoveryFieldsValuesException(
                emailError = InvalidRecoveryFieldsValuesException.Email.EMPTY
            )
        }

        repository.resetPassword(email)
    }

    suspend fun changePassword(
        password: String,
        confirmedPassword: String,
    ) {
        try {
            validatePassword(
                password = password,
                confirmedPassword = confirmedPassword,
            )
        } catch (e: InvalidPasswordValuesException) {
            throw InvalidRecoveryFieldsValuesException(
                passwordError = e.passwordError?.toRecoveryError(),
                confirmedPasswordError = e.confirmedPasswordError?.toRecoveryError(),
            )
        }

        repository.changePassword(password)
    }

    private fun InvalidPasswordValuesException.Password.toRecoveryError() = when (this) {
        InvalidPasswordValuesException.Password.MIN_LENGTH ->
            InvalidRecoveryFieldsValuesException.Password.MIN_LENGTH
    }

    private fun InvalidPasswordValuesException.ConfirmedPassword.toRecoveryError() = when (this) {
        InvalidPasswordValuesException.ConfirmedPassword.MISMATCH ->
            InvalidRecoveryFieldsValuesException.ConfirmedPassword.MISMATCH
    }
}
