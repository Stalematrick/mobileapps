package ru.abramovkirill.languageapp.audition.word_practice.impl.domain.speech

import kotlinx.coroutines.flow.Flow

interface AuditionSpeechRecognizer {
    val results: Flow<AuditionSpeechRecognizerResult>

    suspend fun startRecogniser()
    suspend fun stopRecogniser()
    fun destroy()
}
