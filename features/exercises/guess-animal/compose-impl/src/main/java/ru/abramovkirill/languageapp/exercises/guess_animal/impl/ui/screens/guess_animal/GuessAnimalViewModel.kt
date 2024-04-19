package ru.abramovkirill.languageapp.exercises.guess_animal.impl.ui.screens.guess_animal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import ru.abramovkirill.languageapp.common.utils.launchSafe
import ru.abramovkirill.languageapp.common.utils.painterRes
import ru.abramovkirill.languageapp.common.utils.states.ProcessingState
import ru.abramovkirill.languageapp.common.utils.strRes
import ru.abramovkirill.languageapp.core.design.utils.withReturnToNone
import ru.abramovkirill.languageapp.core.navigation.api.Router
import ru.abramovkirill.languageapp.exercises.guess_animal.impl.domain.EmptyInputException
import ru.abramovkirill.languageapp.exercises.guess_animal.impl.domain.GuessAnimalExercise
import ru.abramovkirill.languageapp.exercises.guess_animal.impl.domain.GuessAnimalUseCase
import ru.abramovkirill.languageapp.exercises.guess_animal.impl.ui.screens.guess_animal.GuessAnimalContract.Intent
import ru.abramovkirill.languageapp.exercises.guess_animal.impl.ui.screens.guess_animal.GuessAnimalContract.SideEffect
import ru.abramovkirill.languageapp.exercises.guess_animal.impl.ui.screens.guess_animal.GuessAnimalContract.State
import ru.abramovkirill.languageapp.core.design.R as DesignR

private typealias IntentBody = SimpleSyntax<State, SideEffect>

internal class GuessAnimalViewModel(
    private val router: Router,
) : ViewModel(), ContainerHost<State, SideEffect> {
    override val container = container<State, SideEffect>(
        initialState = State.Loading
    )

    private var currentExercise: GuessAnimalExercise = GuessAnimalExercise("1", "https://img.razrisyika.ru/kart/84/1200/335577-kartinka-lev-1.jpg")
    private var currentStreak = 0
    private var isCorrect: Boolean = true


    init {
        loadNextExercise()
    }

    fun processIntent(intent: Intent) = intent {
        when (intent) {
            is Intent.OnAnswerTextChange ->
                onAnswerTextChange(intent.text)
            Intent.OnCheckButtonClick ->
                onCheckButtonClick()
            Intent.OnNextButtonClick ->
                onNextButtonClick()
            Intent.OnTryAgainButtonClick ->
                onTryAgainButtonClick()
            Intent.OnGoBackClick ->
                onGoBackClick()

        }
    }

    private fun loadNextExercise() = intent {
        viewModelScope.launchSafe(
            block = {
                reduce { State.Loading }

                val exercise = currentExercise

                reduce {
                    State.Resolving(image = painterRes(exercise.imageUrl))
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

    private suspend fun IntentBody.onAnswerTextChange(text: String) {
        reduce {
            (state as State.Resolving).copy(
                answer = text
            )
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

                postSideEffect(SideEffect.CloseKeyboard)

                val guessResult = GuessAnimalUseCase.Result(
                    isCorrect = isCorrect,
                    correctAnswer = (state as State.Resolving).answer
                )

                isCorrect = !isCorrect

                if (guessResult.isCorrect ) {
                    currentStreak += 1
                    reduce { State.CorrectAnswer }
                } else {
                    currentStreak = 0
                    reduce { State.IncorrectAnswer(correctAnswer = guessResult.correctAnswer) }
                }
            },
            onError = { throwable ->
                withReturnToNone(startWith = ProcessingState.Error) { recoveringState ->
                    reduce {
                        (state as State.Resolving).copy(
                            checkingAnswerState = recoveringState
                        )
                    }
                }

                when (throwable) {
                    is EmptyInputException -> Unit
                    else -> postSideEffect(SideEffect.Message(strRes(DesignR.string.error_smth_went_wrong)))
                }
            }
        )
    }

    private fun onNextButtonClick() {
        loadNextExercise()
    }

    private suspend fun IntentBody.onTryAgainButtonClick() {
        reduce {
            State.Resolving(image = painterRes(requireNotNull(currentExercise).imageUrl))
        }
    }
}
