package ru.abramovkirill.languageapp.profile.impl.ui.screens.select_language

import kotlinx.collections.immutable.ImmutableList
import ru.abramovkirill.languageapp.common.utils.StringResource

internal object SelectLanguageContract {
    sealed interface State {
        val canGoBack: Boolean

        data class Loading(override val canGoBack: Boolean) : State

        data class Loaded(
            override val canGoBack: Boolean,
            val languages: ImmutableList<LanguageItem>,
            val isChoosingLanguage: Boolean = false,
        ) : State
    }

    sealed interface Intent {
        data class OnLanguageSelect(val id: String) : Intent
        data object OnChooseButtonClick : Intent
        data object OnGoBackClick : Intent
    }

    sealed interface SideEffect {
        data class Message(val text: StringResource) : SideEffect
    }
}
