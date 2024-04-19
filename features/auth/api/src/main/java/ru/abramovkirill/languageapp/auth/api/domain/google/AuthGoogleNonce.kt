package ru.abramovkirill.languageapp.auth.api.domain.google

data class AuthGoogleNonce(
    val raw: String,
    val encoded: String,
)
