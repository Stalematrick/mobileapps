package ru.abramovkirill.languageapp.audition.word_practice.impl.ui.screens.word_practice

import ru.abramovkirill.languageapp.common.utils.StringResource

internal object AuditionContract {

    sealed interface State {
        data object Loading : State

        data object Error : State

        data class Resolving(
            val word: String,
            val wordTranscription: String?,
            val result: String = "",
            val step: Step = Step.None,
        ) : State {
            enum class Step {
                None,
                Listening,
                Loading,
                Success,
                Error,
            }
        }
    }

    sealed interface Intent {
        data object OnGoBackClick : Intent
        data object OnNextButtonClick : Intent
        data object OnTryAgainButtonClick : Intent
        data object OnStartListening : Intent
        data object OnStopListening : Intent
        data object OnAudioRecordPermissionGranted : Intent
    }

    sealed interface SideEffect {
        data class Message(val text: StringResource) : SideEffect
        data object RequestAudioRecordPermission : SideEffect
    }
}
