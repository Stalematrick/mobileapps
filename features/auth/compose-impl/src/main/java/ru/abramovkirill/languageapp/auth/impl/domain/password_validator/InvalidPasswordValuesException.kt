package ru.abramovkirill.languageapp.auth.impl.domain.password_validator

data class InvalidPasswordValuesException(
    val passwordError: Password? = null,
    val confirmedPasswordError: ConfirmedPassword? = null,
) : IllegalArgumentException(
    "Invalid sign up fields values: " +
            "password = $passwordError; " +
            "confirmed password = $confirmedPasswordError"
) {
    enum class Password {
        MIN_LENGTH;
    }

    enum class ConfirmedPassword {
        MISMATCH;
    }
}
