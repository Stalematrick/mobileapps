package ru.abramovkirill.languageapp.profile.api.domain

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun getAvailableLanguages(): List<Language>
    suspend fun changeLanguage(language: Language)
    suspend fun isLanguageAlreadySelected(): Boolean
    suspend fun forceTheme(theme: ForcedTheme)
    fun getForcedThemeFlow(): Flow<ForcedTheme?>
}
