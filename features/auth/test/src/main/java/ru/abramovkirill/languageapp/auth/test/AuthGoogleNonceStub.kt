package ru.abramovkirill.languageapp.auth.test

import ru.abramovkirill.languageapp.auth.api.domain.google.AuthGoogleNonce
import ru.abramovkirill.languageapp.auth.api.domain.google.AuthGoogleNonceProvider

object AuthGoogleNonceProviderStub : AuthGoogleNonceProvider {
    override suspend fun provideNonce() = AuthGoogleNonce("", "")
}
