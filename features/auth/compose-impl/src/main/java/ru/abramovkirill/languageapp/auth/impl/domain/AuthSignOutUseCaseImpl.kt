package ru.abramovkirill.languageapp.auth.impl.domain

import ru.abramovkirill.languageapp.auth.api.domain.AuthSignOutUseCase
import ru.abramovkirill.languageapp.auth.api.domain.AuthRepository
import ru.abramovkirill.languageapp.core.profiles.api.domain.ProfilesRepository

internal class AuthSignOutUseCaseImpl(
    private val authRepository: AuthRepository,
    private val profilesRepository: ProfilesRepository,
) : AuthSignOutUseCase {
    override suspend fun invoke() {
        profilesRepository.clearCachedUserProfile()
        authRepository.signOut()
    }
}
