package ru.abramovkirill.languageapp.onboarding.impl.data

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class OnboardingStorage(
    applicationContext: Application,
    private val dispatcher: CoroutineDispatcher
) {
    private val preferences = applicationContext.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    suspend fun getWatchedOnboardingUnits(): Set<String> = withContext(dispatcher) {
        preferences.getStringSet(PREFS_WATCHED_UNITS_KEY, emptySet()) ?: emptySet()
    }

    suspend fun saveWatchedUnit(unit: String) = withContext(dispatcher) {
        preferences.edit {
            putStringSet(
                PREFS_WATCHED_UNITS_KEY,
                preferences.getStringSet(PREFS_WATCHED_UNITS_KEY, emptySet()).with(unit)
            )
        }
    }

    suspend fun saveWatchedUnits(units: List<String>) = withContext(dispatcher) {
        preferences.edit {
            putStringSet(
                PREFS_WATCHED_UNITS_KEY,
                preferences.getStringSet(PREFS_WATCHED_UNITS_KEY, emptySet()).with(units)
            )
        }
    }

    private fun Set<String>?.with(value: String) =
        (this ?: emptySet()) + setOf(value)

    private fun Set<String>?.with(values: List<String>) =
        (this ?: emptySet()) + values

    companion object {
        private const val PREFS_NAME = "onboarding_prefs"
        private const val PREFS_WATCHED_UNITS_KEY = "watched_units_key"
    }
}
