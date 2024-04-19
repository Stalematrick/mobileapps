package ru.abramovkirill.languageapp.exercises.word_practice.impl.ui.screens.word_practice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import ru.abramovkirill.languageapp.common.utils.launchSafe
import ru.abramovkirill.languageapp.common.utils.states.ProcessingState
import ru.abramovkirill.languageapp.common.utils.strRes
import ru.abramovkirill.languageapp.core.design.utils.withReturnToNone
import ru.abramovkirill.languageapp.core.navigation.api.Router
import ru.abramovkirill.languageapp.exercises.word_practice.impl.domain.EmptyInputException
import ru.abramovkirill.languageapp.exercises.word_practice.impl.domain.SourceLanguage
import ru.abramovkirill.languageapp.exercises.word_practice.impl.domain.WordPracticeExercise
import ru.abramovkirill.languageapp.exercises.word_practice.impl.domain.WordPracticeUseCase
import ru.abramovkirill.languageapp.exercises.word_practice.impl.ui.screens.word_practice.WordPracticeContract.Intent
import ru.abramovkirill.languageapp.exercises.word_practice.impl.ui.screens.word_practice.WordPracticeContract.SideEffect
import ru.abramovkirill.languageapp.exercises.word_practice.impl.ui.screens.word_practice.WordPracticeContract.State
import ru.abramovkirill.languageapp.core.design.R as DesignR

private typealias IntentBody = SimpleSyntax<State, SideEffect>

internal class WordPracticeViewModel(
    private val router: Router,
    private val wordPractice: WordPracticeUseCase,
) : ViewModel(), ContainerHost<State, SideEffect> {
    override val container = container<State, SideEffect>(
        initialState = State.Loading
    )

    private var currentExercise: WordPracticeExercise = WordPracticeExercise(
        "1",
        "Test",
            "[tanscription]",
            answers = listOf("answer")
        )

    private var currentStreak = 0
    private var currentExerciseLanguage = SourceLanguage.Russian
    init {
        loadNextExercise()
    }

    fun processIntent(intent: Intent) = intent {
        when (intent) {
            is Intent.OnAnswerClick ->
                onAnswerClickChange(intent.answer)
            Intent.OnCheckButtonClick ->
                onCheckButtonClick()
            Intent.OnNextButtonClick ->
                onNextButtonClick()
            Intent.OnGoBackClick ->
                onGoBackClick()
        }
    }

    private fun loadNextExercise() = intent {
        viewModelScope.launchSafe(
            block = {
                reduce { State.Loading }

                val exercise = currentExercise

                updateCurrentExerciseLanguage()

                reduce {
                    State.Resolving(
                        word = exercise.word,
                        wordTranscription = exercise.wordTranscription,
                        answers = exercise.answers
                            .map { word -> WordPracticeAnswerItem(word = word) }
                            .toImmutableList(),
                    )
                }
            },
            onError = {
                postSideEffect(SideEffect.Message(strRes(DesignR.string.error_smth_went_wrong)))
                reduce { State.Error }
            }
        )
    }

    private suspend fun onGoBackClick() {
        router.navigateBack()
    }

    private suspend fun IntentBody.onAnswerClickChange(clickedAnswer: WordPracticeAnswerItem) {
        reduce {
            (state as State.Resolving).let { state ->
                state.copy(answers = state.answers.withUserSelection(clickedAnswer.word))
            }
        }
    }

    private suspend fun IntentBody.onCheckButtonClick() {
        viewModelScope.launchSafe(
            block = {
                reduce {
                    (state as State.Resolving).copy(
                        checkingAnswerState = ProcessingState.InProgress
                    )
                }

                val selectedAnswer = (state as State.Resolving).answers.getUserSelectedAnswer()
                    ?: throw EmptyInputException()

                val exerciseResult = wordPractice(
                    streak = currentStreak,
                    exerciseId = requireNotNull(currentExercise).id,
                    answer = selectedAnswer.word,
                )

                currentStreak = if (exerciseResult.isCorrect) currentStreak + 1 else 0

                withReturnToNone(
                    startWith =
                        if (exerciseResult.isCorrect) ProcessingState.Success
                        else ProcessingState.Error,
                    onStartState = { checkingAnswerState ->
                        reduce {
                            (state as State.Resolving).let { state ->
                                state.copy(
                                    isResolved = true,
                                    checkingAnswerState = checkingAnswerState,
                                    answers = state.answers.withCorrectAnswer(
                                        userAnswerWord = selectedAnswer.word,
                                        correctAnswerWord = exerciseResult.correctAnswer,
                                    )
                                )
                            }
                        }
                    },
                    onEndState = { checkingAnswerState ->
                        reduce {
                            (state as State.Resolving).copy(
                                checkingAnswerState = checkingAnswerState
                            )
                        }
                    }
                )
            },
            onError = { throwable ->
                withReturnToNone(startWith = ProcessingState.Error) { checkingAnswerState ->
                    reduce {
                        (state as State.Resolving).copy(
                            checkingAnswerState = checkingAnswerState
                        )
                    }
                }

                when(throwable) {
                    is EmptyInputException -> Unit
                    else -> postSideEffect(SideEffect.Message(strRes(throwable.stackTraceToString())))
                }
            }
        )
    }

    private fun onNextButtonClick() {
        loadNextExercise()
    }

    private fun updateCurrentExerciseLanguage() {
        val currentLanguageIndex = SourceLanguage.entries.indexOf(currentExerciseLanguage)
        val nextLanguageIndex = (currentLanguageIndex + 1) % SourceLanguage.entries.size
        currentExerciseLanguage = SourceLanguage.entries[nextLanguageIndex]
    }

    private fun ImmutableList<WordPracticeAnswerItem>.withUserSelection(
        userSelectedWord: String
    ) =
        map { answer ->
            answer.copy(
                selectionType = if (userSelectedWord == answer.word) {
                    WordPracticeAnswerSelectionType.User
                } else {
                    WordPracticeAnswerSelectionType.None
                }
            )
        }.toImmutableList()

    private fun ImmutableList<WordPracticeAnswerItem>.withCorrectAnswer(
        userAnswerWord: String,
        correctAnswerWord: String,
    ) =
        map { answer ->
            answer.copy(
                selectionType = when (answer.word) {
                    correctAnswerWord ->
                        WordPracticeAnswerSelectionType.Correct
                    userAnswerWord ->
                        WordPracticeAnswerSelectionType.Incorrect
                    else ->
                        WordPracticeAnswerSelectionType.None
                }
            )
        }.toImmutableList()

    private fun ImmutableList<WordPracticeAnswerItem>.getUserSelectedAnswer() =
        find { answer -> answer.selectionType == WordPracticeAnswerSelectionType.User }
}
