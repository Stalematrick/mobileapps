package ru.abramovkirill.languageapp.auth.impl.domain.sign_in

import ru.abramovkirill.languageapp.auth.api.domain.AuthRepository
import ru.abramovkirill.languageapp.core.profiles.api.domain.Profile
import ru.abramovkirill.languageapp.core.profiles.api.domain.ProfilesRepository

class SignInWithGoogleUseCase(
    private val authRepository: AuthRepository,
    private val profilesRepository: ProfilesRepository,
) {
    suspend operator fun invoke(
        rawNonce: String,
        idToken: String,
        email: String,
        firstName: String,
        lastName: String,
    ) {
        val user = authRepository.signInWithGoogle(
            rawNonce = rawNonce,
            idToken = idToken,
            email = email,
            firstName = firstName,
            lastName = lastName,
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
