package ru.abramovkirill.languageapp.profile.impl.ui.screens.select_language

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toImmutableList
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import ru.abramovkirill.languageapp.common.utils.launchSafe
import ru.abramovkirill.languageapp.common.utils.letIfInheritor
import ru.abramovkirill.languageapp.common.utils.strRes
import ru.abramovkirill.languageapp.core.navigation.api.Router
import ru.abramovkirill.languageapp.profile.api.domain.Language
import ru.abramovkirill.languageapp.profile.api.domain.SettingsRepository
import ru.abramovkirill.languageapp.profile.impl.mappers.toDomain
import ru.abramovkirill.languageapp.profile.impl.mappers.toUiItem
import ru.abramovkirill.languageapp.profile.impl.ui.screens.select_language.SelectLanguageContract.Intent
import ru.abramovkirill.languageapp.profile.impl.ui.screens.select_language.SelectLanguageContract.SideEffect
import ru.abramovkirill.languageapp.profile.impl.ui.screens.select_language.SelectLanguageContract.State
import ru.abramovkirill.languageapp.core.design.R as DesignR

private typealias IntentBody = SimpleSyntax<State, SideEffect>

internal class SelectLanguageViewModel(
    private val router: Router,
    private val settingsRepository: SettingsRepository,
    private val canGoBack: Boolean,
) : ViewModel(), ContainerHost<State, SideEffect> {
    override val container = container<State, SideEffect>(
        initialState = State.Loading(canGoBack = canGoBack)
    )

    init {
        loadLanguages()
    }

    fun processIntent(intent: Intent) = intent {
        when (intent) {
            Intent.OnGoBackClick ->
                onGoBackClick()
            Intent.OnChooseButtonClick ->
                onChooseButtonClick()
            is Intent.OnLanguageSelect ->
                onLanguageSelectButtonClick(intent.id)
        }
    }

    private fun loadLanguages() = intent {
        viewModelScope.launchSafe(
            block = {
                reduce { State.Loading(canGoBack = canGoBack) }

                val languages = settingsRepository.getAvailableLanguages()

                reduce {
                    State.Loaded(
                        canGoBack = canGoBack,
                        languages = languages
                            .map(Language::toUiItem)
                            .toImmutableList()
                    )
                }
            },
            onError = {
                postSideEffect(SideEffect.Message(strRes(DesignR.string.error_smth_went_wrong)))
                router.navigateBack()
            }
        )
    }

    private suspend fun onGoBackClick() {
        router.navigateBack()
    }

    private suspend fun IntentBody.onChooseButtonClick() {
        viewModelScope.launchSafe(
            block = {
                reduce { (state as State.Loaded).copy(isChoosingLanguage = true) }

                val languageItem = (state as State.Loaded).languages
                    .find { language -> language.isSelected }
                    ?: throw IllegalStateException("No selected language found")

                val language = languageItem.toDomain()
                    ?: throw IllegalStateException("Unknown language")

                reduce { (state as State.Loaded).copy(isChoosingLanguage = false) }

                router.navigateBack()

                settingsRepository.changeLanguage(language)
            },
            onError = {
                reduce { (state as State.Loaded).copy(isChoosingLanguage = false) }
                postSideEffect(SideEffect.Message(strRes(DesignR.string.error_smth_went_wrong)))
            }
        )
    }

    private suspend fun IntentBody.onLanguageSelectButtonClick(id: String) {
        reduce {
            state.letIfInheritor<State, State.Loaded> { state ->
                state.copy(
                    languages = state.languages
                        .map { language -> language.copy(isSelected = language.id == id) }
                        .toImmutableList(),
                )
            }
        }
    }
}
