package ru.abramovkirill.languageapp.auth.impl.domain.sign_up

import ru.abramovkirill.languageapp.auth.api.domain.AuthRepository
import ru.abramovkirill.languageapp.auth.impl.domain.password_validator.InvalidPasswordValuesException
import ru.abramovkirill.languageapp.auth.impl.domain.password_validator.PasswordValidator

class SignUpUseCase(
    private val repository: AuthRepository,
    private val validatePassword: PasswordValidator,
) {
    suspend operator fun invoke(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        confirmedPassword: String,
    ) {
        validate(
            firstName = firstName,
            lastName = lastName,
            email = email,
            password = password,
            confirmedPassword = confirmedPassword,
        )

        repository.signUp(
            firstName = firstName.trim(),
            lastName = lastName.trim(),
            email = email.trim(),
            password = password.trim(),
        )
    }

    fun validate(
        firstName: String = "",
        lastName: String = "",
        email: String = "",
        password: String = "",
        confirmedPassword: String = "",
        fieldsToValidate: List<Field> = Field.entries,
    ) {
        val errorBuilder = ErrorBuilder()

        if (Field.FIRST_NAME in fieldsToValidate && firstName.isBlank()) {
            errorBuilder.transform { error ->
                error.copy(firstNameError = InvalidSignUpFieldsValuesException.Name.EMPTY)
            }
        }

        if (Field.LAST_NAME in fieldsToValidate && lastName.isBlank()) {
            errorBuilder.transform { error ->
                error.copy(lastNameError = InvalidSignUpFieldsValuesException.Name.EMPTY)
            }
        }

        if (Field.EMAIL in fieldsToValidate && !email.matches(Regex(EMAIL_REGEX_PATTERN))) {
            errorBuilder.transform { error ->
                error.copy(emailError = InvalidSignUpFieldsValuesException.Email.INVALID_FORMAT)
            }
        }

        if (Field.PASSWORD in fieldsToValidate) {
            try {
                validatePassword(
                    password = password,
                    confirmedPassword = confirmedPassword
                )
            } catch (e: InvalidPasswordValuesException) {
                errorBuilder.transform { error ->
                    error.copy(
                        passwordError = e.passwordError?.toSignUpError(),
                        confirmedPasswordError = e.confirmedPasswordError?.toSignUpError(),
                    )
                }
            }
        }

        errorBuilder.validationError?.let { error ->
            throw error
        }
    }

    private class ErrorBuilder {
        var validationError: InvalidSignUpFieldsValuesException? = null
            private set

        fun transform(
            transformer: (InvalidSignUpFieldsValuesException) -> InvalidSignUpFieldsValuesException
        ) {
            validationError = transformer(validationError ?: InvalidSignUpFieldsValuesException())
        }
    }

    private fun InvalidPasswordValuesException.Password.toSignUpError() = when (this) {
        InvalidPasswordValuesException.Password.MIN_LENGTH ->
            InvalidSignUpFieldsValuesException.Password.MIN_LENGTH
    }

    private fun InvalidPasswordValuesException.ConfirmedPassword.toSignUpError() = when (this) {
        InvalidPasswordValuesException.ConfirmedPassword.MISMATCH ->
            InvalidSignUpFieldsValuesException.ConfirmedPassword.MISMATCH
    }

    enum class Field {
        FIRST_NAME,
        LAST_NAME,
        EMAIL,
        PASSWORD,
    }

    companion object {
        private const val EMAIL_REGEX_PATTERN = ("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$")
    }
}
