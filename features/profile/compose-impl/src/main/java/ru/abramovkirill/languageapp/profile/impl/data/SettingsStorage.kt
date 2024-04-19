package ru.abramovkirill.languageapp.profile.impl.data

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.abramovkirill.languageapp.profile.api.domain.ForcedTheme
import ru.abramovkirill.languageapp.profile.api.domain.Language

class SettingsStorage(
    applicationContext: Application,
    private val storageDispatcher: CoroutineDispatcher,
) {
    private val preferences = applicationContext.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    suspend fun saveLanguage(language: Language) =
        withContext(storageDispatcher) {
            preferences.edit {
                putString(PREFS_LANGUAGE_KEY, language.name)
            }
        }

    suspend fun getSavedLanguage(): Language?  =
        withContext(storageDispatcher) {
            preferences.getString(PREFS_LANGUAGE_KEY, null)?.let { name ->
                Language.entries.find { language -> language.name == name }
            }
        }

    suspend fun saveForcedTheme(forcedTheme: ForcedTheme) =
        withContext(storageDispatcher) {
            preferences.edit {
                putString(PREFS_FORCED_THEME_KEY, forcedTheme.name)
            }
        }

    suspend fun getForcedTheme(): ForcedTheme? =
        withContext(storageDispatcher) {
            preferences.getString(PREFS_FORCED_THEME_KEY, null)?.let { themeName ->
                ForcedTheme.valueOf(themeName)
            }
        }

    fun getForcedThemeFlow(): Flow<ForcedTheme?> =
        callbackFlow {
            val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                if (key == PREFS_FORCED_THEME_KEY) {
                    launch { trySend(getForcedTheme()) }
                }
            }

            preferences.registerOnSharedPreferenceChangeListener(listener)

            launch { trySend(getForcedTheme()) }

            awaitClose {
                preferences.unregisterOnSharedPreferenceChangeListener(listener)
            }
        }

    companion object {
        private const val PREFS_NAME = "settings_prefs"
        private const val PREFS_LANGUAGE_KEY = "language_key"
        private const val PREFS_FORCED_THEME_KEY = "forced_theme_key"
    }
}
