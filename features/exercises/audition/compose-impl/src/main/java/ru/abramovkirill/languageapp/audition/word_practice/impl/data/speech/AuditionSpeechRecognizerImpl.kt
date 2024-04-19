package ru.abramovkirill.languageapp.audition.word_practice.impl.data.speech

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.withContext
import ru.abramovkirill.languageapp.audition.word_practice.impl.domain.speech.AuditionSpeechRecognizer
import ru.abramovkirill.languageapp.audition.word_practice.impl.domain.speech.AuditionSpeechRecognizerResult

class AuditionSpeechRecognizerImpl(
    applicationContext: Application,
    private val recognizerDispatcher: CoroutineDispatcher,
) : AuditionSpeechRecognizer {
    private val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(applicationContext).apply {
        setRecognitionListener(object : RecognitionListener {
            override fun onError(error: Int) {
                _results.trySend(AuditionSpeechRecognizerResult.Error)
            }

            override fun onResults(results: Bundle?) {
                val text = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()

                if (text != null) {
                    _results.trySend(AuditionSpeechRecognizerResult.Success(text))
                } else {
                    _results.trySend(AuditionSpeechRecognizerResult.Error)
                }
            }

            override fun onReadyForSpeech(params: Bundle?) = Unit
            override fun onRmsChanged(rmsdB: Float) = Unit
            override fun onBufferReceived(buffer: ByteArray?) = Unit
            override fun onPartialResults(partialResults: Bundle?) = Unit
            override fun onEvent(eventType: Int, params: Bundle?) = Unit
            override fun onBeginningOfSpeech() = Unit
            override fun onEndOfSpeech() = Unit
        })
    }

    private val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
    }

    private val _results = Channel<AuditionSpeechRecognizerResult>(Channel.BUFFERED)
    override val results = _results.receiveAsFlow()

    override suspend fun startRecogniser() {
        withContext(recognizerDispatcher) {
            speechRecognizer.startListening(speechRecognizerIntent)
        }
    }

    override suspend fun stopRecogniser() {
        withContext(recognizerDispatcher) {
            speechRecognizer.stopListening()
        }
    }

    override fun destroy() {
        speechRecognizer.destroy()
    }
}
