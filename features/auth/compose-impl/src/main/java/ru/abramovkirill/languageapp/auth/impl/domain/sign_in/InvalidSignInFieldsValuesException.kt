package ru.abramovkirill.languageapp.auth.impl.domain.sign_in

class InvalidSignInFieldsValuesException(
    val emailError: Email? = null,
    val passwordError: Password? = null,
) : IllegalArgumentException("Invalid sign in fields values: email = $emailError; password = $passwordError") {
    enum class Email {
        EMPTY
    }

    enum class Password {
        EMPTY
    }
}
