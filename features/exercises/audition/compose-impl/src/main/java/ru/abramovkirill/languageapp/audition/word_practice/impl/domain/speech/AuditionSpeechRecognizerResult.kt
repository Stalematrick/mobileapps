package ru.abramovkirill.languageapp.audition.word_practice.impl.domain.speech

sealed interface AuditionSpeechRecognizerResult {
    data object Error : AuditionSpeechRecognizerResult
    data class Success(val text: String) : AuditionSpeechRecognizerResult
}
