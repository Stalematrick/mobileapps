package ru.abramovkirill.languageapp.auth.test

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.abramovkirill.languageapp.auth.api.domain.AuthEvent
import ru.abramovkirill.languageapp.auth.api.domain.AuthRepository
import ru.abramovkirill.languageapp.auth.api.domain.User

object AuthRepositoryStub : AuthRepository {
    override val authEvents: Flow<AuthEvent> = emptyFlow()

    private val userStub = User("", null, null, null)

    override suspend fun signIn(email: String, password: String) = userStub
    override suspend fun awaitInitialization() = Unit
    override suspend fun saveSession() = Unit
    override suspend fun hasSavedSession() = false
    override suspend fun resetPassword(email: String) = Unit
    override suspend fun applyRecoveryCode(code: String) = Unit
    override suspend fun changePassword(password: String) = Unit
    override suspend fun signOut() = Unit

    override suspend fun signInWithGoogle(
        idToken: String,
        email: String,
        firstName: String,
        lastName: String,
        rawNonce: String
    ) = userStub

    override suspend fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ) = Unit
}
