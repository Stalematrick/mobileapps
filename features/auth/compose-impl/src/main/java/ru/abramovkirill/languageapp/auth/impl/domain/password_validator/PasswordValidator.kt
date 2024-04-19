package ru.abramovkirill.languageapp.auth.impl.domain.password_validator

class PasswordValidator {
    operator fun invoke(
        password: String,
        confirmedPassword: String,
    ) {
        val errorBuilder = ErrorBuilder()

        if (password.trim().length < MIN_PASSWORD_LENGTH) {
            errorBuilder.transform { error ->
                error.copy(passwordError = InvalidPasswordValuesException.Password.MIN_LENGTH)
            }
        }

        if (password.trim() != confirmedPassword.trim()) {
            errorBuilder.transform { error ->
                error.copy(confirmedPasswordError = InvalidPasswordValuesException.ConfirmedPassword.MISMATCH)
            }
        }

        errorBuilder.validationError?.let { error -> throw error }
    }

    private class ErrorBuilder {
        var validationError: InvalidPasswordValuesException? = null
            private set

        fun transform(
            transformer: (InvalidPasswordValuesException) -> InvalidPasswordValuesException
        ) {
            validationError = transformer(validationError ?: InvalidPasswordValuesException())
        }
    }

    companion object {
        private const val MIN_PASSWORD_LENGTH = 6
    }
}
