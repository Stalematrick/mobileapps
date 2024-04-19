package ru.abramovkirill.languageapp.profile.impl.ui.screens.avatar_crop

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import ru.abramovkirill.languageapp.common.utils.PainterResource
import ru.abramovkirill.languageapp.common.utils.StringResource
import ru.abramovkirill.languageapp.common.utils.states.ProcessingState

internal object AvatarCropContract {
    data class State(
        val avatar: PainterResource,
        val savingState: ProcessingState = ProcessingState.None,
    )

    sealed interface Intent {
        data object OnGoBackClick : Intent
        data object OnSaveClick : Intent

        data class OnAvatarCropChanged(
            val offset: IntOffset,
            val size: IntSize
        ) : Intent
    }

    sealed interface SideEffect {
        data class Message(val text: StringResource) : SideEffect
    }
}
