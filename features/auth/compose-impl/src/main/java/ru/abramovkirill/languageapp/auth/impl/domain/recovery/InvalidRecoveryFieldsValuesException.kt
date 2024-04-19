package ru.abramovkirill.languageapp.auth.impl.domain.recovery

class InvalidRecoveryFieldsValuesException(
    val emailError: Email? = null,
    val passwordError: Password? = null,
    val confirmedPasswordError: ConfirmedPassword? = null,
) : IllegalArgumentException(
    "Invalid recovery fields values: email = $emailError; " +
            "password = $passwordError; " +
            "confirmedPassword = $confirmedPasswordError"
) {
    enum class Email {
        EMPTY
    }

    enum class Password {
        MIN_LENGTH;
    }

    enum class ConfirmedPassword {
        MISMATCH;
    }
}
