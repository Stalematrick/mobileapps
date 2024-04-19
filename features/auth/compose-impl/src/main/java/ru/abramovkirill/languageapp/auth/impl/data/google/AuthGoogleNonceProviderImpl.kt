package ru.abramovkirill.languageapp.auth.impl.data.google

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.abramovkirill.languageapp.auth.api.domain.google.AuthGoogleNonce
import ru.abramovkirill.languageapp.auth.api.domain.google.AuthGoogleNonceProvider
import java.security.MessageDigest
import java.util.UUID

class AuthGoogleNonceProviderImpl(
    private val dispatcher: CoroutineDispatcher,
) : AuthGoogleNonceProvider {

    override suspend fun provideNonce() = withContext(dispatcher) {
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val messageDigest = MessageDigest.getInstance(GOOGLE_NONCE_ENCODING_TYPE)
        val digest = messageDigest.digest(bytes)
        val encodedNonce = digest.fold("") { accumulator, next -> accumulator + "%02x".format(next) }

        AuthGoogleNonce(
            raw = rawNonce,
            encoded = encodedNonce
        )
    }

    companion object {
        private const val GOOGLE_NONCE_ENCODING_TYPE = "SHA-256"
    }
}
