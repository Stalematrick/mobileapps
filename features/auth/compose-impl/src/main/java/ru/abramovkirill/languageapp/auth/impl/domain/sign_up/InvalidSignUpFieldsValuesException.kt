package ru.abramovkirill.languageapp.auth.impl.domain.sign_up

data class InvalidSignUpFieldsValuesException(
    val firstNameError: Name? = null,
    val lastNameError: Name? = null,
    val emailError: Email? = null,
    val passwordError: Password? = null,
    val confirmedPasswordError: ConfirmedPassword? = null,
) : IllegalArgumentException(
    "Invalid sign up fields values: " +
            "first name = $firstNameError; " +
            "last name = $lastNameError; " +
            "email = $emailError; " +
            "password = $passwordError; " +
            "confirmed password = $confirmedPasswordError"
) {
    enum class Name {
        EMPTY;
    }

    enum class Email {
        INVALID_FORMAT;
    }

    enum class Password {
        MIN_LENGTH;
    }

    enum class ConfirmedPassword {
        MISMATCH;
    }
}
