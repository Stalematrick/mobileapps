package ru.abramovkirill.languageapp.audition.word_practice.impl.ui.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import ru.abramovkirill.languageapp.core.navigation.compose_impl.Route
import ru.abramovkirill.languageapp.exercises.audition.api.ui.navigation.AUDITION_SCREEN_ROUTE_PATH
import ru.abramovkirill.languageapp.audition.word_practice.impl.di.AuditionComponent
import ru.abramovkirill.languageapp.audition.word_practice.impl.ui.screens.word_practice.WordPracticeScreen

object AuditionScreenRoute : Route.Screen(
    path = AUDITION_SCREEN_ROUTE_PATH,
) {
    @Composable
    override fun AnimatedContentScope.Content(
        navController: NavController,
        navBackStackEntry: NavBackStackEntry
    ) {
        WordPracticeScreen(
            viewModel = viewModel(
                factory = AuditionComponent.wordPracticeViewModelFactory
            )
        )
    }
}
