package ru.abramovkirill.languageapp.exercises.word_practice.impl.ui.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import ru.abramovkirill.languageapp.core.navigation.compose_impl.Route
import ru.abramovkirill.languageapp.exercises.word_practice.api.ui.navigation.WORD_PRACTICE_SCREEN_ROUTE_PATH
import ru.abramovkirill.languageapp.exercises.word_practice.impl.di.WordPracticeComponent
import ru.abramovkirill.languageapp.exercises.word_practice.impl.ui.screens.word_practice.WordPracticeScreen

object WordPracticeScreenRoute : Route.Screen(
    path = WORD_PRACTICE_SCREEN_ROUTE_PATH,
) {
    @Composable
    override fun AnimatedContentScope.Content(
        navController: NavController,
        navBackStackEntry: NavBackStackEntry
    ) {
        WordPracticeScreen(
            viewModel = viewModel(
                factory = WordPracticeComponent.wordPracticeViewModelFactory
            )
        )
    }
}
