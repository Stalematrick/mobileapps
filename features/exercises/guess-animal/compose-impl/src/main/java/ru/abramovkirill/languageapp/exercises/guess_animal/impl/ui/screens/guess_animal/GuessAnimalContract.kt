package ru.abramovkirill.languageapp.exercises.guess_animal.impl.ui.screens.guess_animal

import ru.abramovkirill.languageapp.common.utils.PainterResource
import ru.abramovkirill.languageapp.common.utils.StringResource
import ru.abramovkirill.languageapp.common.utils.states.ProcessingState

internal object GuessAnimalContract {
    sealed interface State {
        data object Loading : State

        data class Resolving(
            val image: PainterResource,
            val answer: String = "",
            val checkingAnswerState: ProcessingState = ProcessingState.None
        ) : State

        data object CorrectAnswer : State

        data class IncorrectAnswer(val correctAnswer: String) : State

        data object Error : State
    }

    sealed interface Intent {
        data class OnAnswerTextChange(val text: String) : Intent
        data object OnGoBackClick : Intent
        data object OnCheckButtonClick : Intent
        data object OnNextButtonClick : Intent
        data object OnTryAgainButtonClick : Intent
    }

    sealed interface SideEffect {
        data class Message(val text: StringResource) : SideEffect
        data object CloseKeyboard : SideEffect
    }
}
