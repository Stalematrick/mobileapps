package ru.abramovkirill.languageapp.exercises.word_practice.impl.ui.screens.word_practice

import kotlinx.collections.immutable.ImmutableList
import ru.abramovkirill.languageapp.common.utils.StringResource
import ru.abramovkirill.languageapp.common.utils.states.ProcessingState

internal object WordPracticeContract {

    sealed interface State {
        data object Loading : State

        data object Error : State

        data class Resolving(
            val word: String,
            val wordTranscription: String?,
            val isResolved: Boolean = false,
            val answers: ImmutableList<WordPracticeAnswerItem>,
            val checkingAnswerState: ProcessingState = ProcessingState.None,
        ) : State
    }

    sealed interface Intent {
        data class OnAnswerClick(val answer: WordPracticeAnswerItem) : Intent
        data object OnGoBackClick : Intent
        data object OnCheckButtonClick : Intent
        data object OnNextButtonClick : Intent
    }

    sealed interface SideEffect {
        data class Message(val text: StringResource) : SideEffect
    }
}
