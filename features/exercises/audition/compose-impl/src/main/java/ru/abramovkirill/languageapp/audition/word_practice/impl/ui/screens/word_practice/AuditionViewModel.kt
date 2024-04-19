package ru.abramovkirill.languageapp.audition.word_practice.impl.ui.screens.word_practice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import ru.abramovkirill.languageapp.audition.word_practice.impl.domain.AuditionExercise
import ru.abramovkirill.languageapp.audition.word_practice.impl.domain.AuditionUseCase
import ru.abramovkirill.languageapp.audition.word_practice.impl.domain.EmptyInputException
import ru.abramovkirill.languageapp.audition.word_practice.impl.domain.speech.AuditionSpeechRecognizer
import ru.abramovkirill.languageapp.audition.word_practice.impl.domain.speech.AuditionSpeechRecognizerResult
import ru.abramovkirill.languageapp.audition.word_practice.impl.ui.screens.word_practice.AuditionContract.Intent
import ru.abramovkirill.languageapp.audition.word_practice.impl.ui.screens.word_practice.AuditionContract.SideEffect
import ru.abramovkirill.languageapp.audition.word_practice.impl.ui.screens.word_practice.AuditionContract.State
import ru.abramovkirill.languageapp.common.utils.launchSafe
import ru.abramovkirill.languageapp.common.utils.strRes
import ru.abramovkirill.languageapp.core.navigation.api.Router
import ru.abramovkirill.languageapp.core.design.R as DesignR

private typealias IntentBody = SimpleSyntax<State, SideEffect>

internal class AuditionViewModel(
    private val router: Router,
    private val wordPractice: AuditionUseCase,
    private val speechRecognizer: AuditionSpeechRecognizer,
) : ViewModel(), ContainerHost<State, SideEffect> {
    override val container = container<State, SideEffect>(
        initialState = State.Loading
    )

    private var currentExercise: AuditionExercise = AuditionExercise("1", "Word", "Word")

    private var currentStreak = 0

    init {
        loadNextExercise()
        observeSpeechRecognitionResults()
    }

    override fun onCleared() {
        speechRecognizer.destroy()
        super.onCleared()
    }

    fun processIntent(intent: Intent) = intent {
        when (intent) {
            Intent.OnNextButtonClick ->
                onNextButtonClick()
            Intent.OnGoBackClick ->
                onGoBackClick()
            Intent.OnTryAgainButtonClick ->
                onTryAgainButtonClick()
            Intent.OnStartListening ->
                onStartListening()
            Intent.OnStopListening ->
                onStopListening()
            Intent.OnAudioRecordPermissionGranted ->
                onAudioRecordPermissionGranted()
        }
    }

    private fun observeSpeechRecognitionResults() {
        speechRecognizer.results
            .onEach { result ->
                when (result) {
                    AuditionSpeechRecognizerResult.Error ->
                        processSpeechRecognizerError()
                    is AuditionSpeechRecognizerResult.Success ->
                        processSpeechRecognizerResult(result.text)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun loadNextExercise() = intent {
        viewModelScope.launchSafe(
            block = {
                reduce { State.Loading }

                val exercise = currentExercise

                reduce {
                    State.Resolving(
                        word = exercise.word,
                        wordTranscription = exercise.wordTranscription,
                    )
                }
            },
            onError = {
                postSideEffect(SideEffect.Message(strRes(DesignR.string.error_smth_went_wrong)))
                reduce { State.Error }
            }
        )
    }

    private suspend fun IntentBody.onTryAgainButtonClick() {
        currentExercise?.let { exercise ->
            reduce {
                State.Resolving(
                    word = exercise.word,
                    wordTranscription = exercise.wordTranscription,
                )
            }
        }
    }

    private suspend fun onGoBackClick() {
        router.navigateBack()
    }

    private suspend fun IntentBody.onStartListening() {
        postSideEffect(SideEffect.RequestAudioRecordPermission)
    }

    private suspend fun IntentBody.onAudioRecordPermissionGranted() {
        speechRecognizer.startRecogniser()

        reduce {
            (state as State.Resolving).copy(
                step = State.Resolving.Step.Listening
            )
        }
    }

    private suspend fun onStopListening() {
        speechRecognizer.stopRecogniser()
    }

    private fun processSpeechRecognizerResult(answer: String) = intent {
        viewModelScope.launchSafe(
            block = {
                reduce {
                    (state as State.Resolving).copy(
                        step = State.Resolving.Step.Loading,
                        result = answer,
                    )
                }

                if (answer.isBlank()) {
                    throw EmptyInputException()
                }

                val exerciseResult = wordPractice(
                    streak = currentStreak,
                    correctAnswer = requireNotNull(currentExercise).word,
                    answer = answer,
                )

                currentStreak = if (exerciseResult.isCorrect) currentStreak + 1 else 0

                reduce {
                    (state as State.Resolving).copy(
                        step = if (exerciseResult.isCorrect) {
                            State.Resolving.Step.Success
                        } else {
                            State.Resolving.Step.Error
                        }
                    )
                }
            },
            onError = { throwable ->
                reduce {
                    (state as State.Resolving).copy(
                        step = State.Resolving.Step.Error
                    )
                }

                when(throwable) {
                    is EmptyInputException -> Unit
                    else -> postSideEffect(SideEffect.Message(strRes(DesignR.string.error_smth_went_wrong)))
                }
            }
        )
    }

    private fun processSpeechRecognizerError() = intent {
        reduce {
            (state as State.Resolving).copy(
                step = State.Resolving.Step.Error
            )
        }
    }

    private fun onNextButtonClick() {
        loadNextExercise()
    }
}
