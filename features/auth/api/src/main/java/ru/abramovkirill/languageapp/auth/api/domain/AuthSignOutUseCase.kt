package ru.abramovkirill.languageapp.auth.api.domain

interface AuthSignOutUseCase {
    suspend operator fun invoke()
}
