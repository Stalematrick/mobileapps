package ru.abramovkirill.languageapp.auth.api.domain.google

interface AuthGoogleNonceProvider {
    suspend fun provideNonce(): AuthGoogleNonce
}
