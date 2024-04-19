package ru.abramovkirill.languageapp.profile.impl.data

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import kotlinx.coroutines.flow.Flow
import ru.abramovkirill.languageapp.profile.api.domain.ForcedTheme
import ru.abramovkirill.languageapp.profile.api.domain.Language
import ru.abramovkirill.languageapp.profile.api.domain.SettingsRepository

internal class SettingsRepositoryImpl(
    private val settingsStorage: SettingsStorage,
) : SettingsRepository {
    override suspend fun getAvailableLanguages(): List<Language> {
        return Language.entries
    }

    override suspend fun changeLanguage(language: Language) {
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(language.tag)
        AppCompatDelegate.setApplicationLocales(appLocale)
        settingsStorage.saveLanguage(language)
    }

    override suspend fun isLanguageAlreadySelected(): Boolean {
        return settingsStorage.getSavedLanguage() != null
    }

    override fun getForcedThemeFlow(): Flow<ForcedTheme?> {
        return settingsStorage.getForcedThemeFlow()
    }

    override suspend fun forceTheme(theme: ForcedTheme) {
        settingsStorage.saveForcedTheme(theme)
    }
}
