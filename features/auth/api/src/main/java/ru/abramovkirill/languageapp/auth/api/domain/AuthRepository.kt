package ru.abramovkirill.languageapp.auth.api.domain

import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    val authEvents: Flow<AuthEvent>

    suspend fun awaitInitialization()

    suspend fun saveSession()

    suspend fun hasSavedSession(): Boolean

    suspend fun signIn(
        email: String,
        password: String,
    ): User

    suspend fun signInWithGoogle(
        idToken: String,
        email: String,
        firstName: String,
        lastName: String,
        rawNonce: String
    ): User

    suspend fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
    )

    suspend fun resetPassword(email: String)

    suspend fun applyRecoveryCode(code: String)

    suspend fun changePassword(password: String)

    suspend fun signOut()
}
