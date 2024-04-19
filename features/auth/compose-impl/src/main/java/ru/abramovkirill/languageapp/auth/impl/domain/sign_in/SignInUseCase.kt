package ru.abramovkirill.languageapp.auth.impl.domain.sign_in

import ru.abramovkirill.languageapp.auth.api.domain.AuthRepository
import ru.abramovkirill.languageapp.core.profiles.api.domain.Profile
import ru.abramovkirill.languageapp.core.profiles.api.domain.ProfilesRepository

class SignInUseCase(
    private val authRepository: AuthRepository,
    private val profilesRepository: ProfilesRepository,
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ) {
        if (email.isBlank() || password.isBlank()) {
            throw InvalidSignInFieldsValuesException(
                emailError = if (email.isBlank()) {
                    InvalidSignInFieldsValuesException.Email.EMPTY
                } else {
                    null
                },
                passwordError = if (password.isBlank()) {
                    InvalidSignInFieldsValuesException.Password.EMPTY
                } else {
                    null
                },
            )
        }

        val user = authRepository.signIn(
            email = email.trim(),
            password = password.trim()
        )

        profilesRepository.createProfile(
            Profile(
                id = user.id,
                avatarUrl = user.avatarUrl,
                firstName = user.firstName,
                lastName = user.lastName,
                email = email,
                totalScore = 0f,
            )
        )

        authRepository.saveSession()
    }
}
