package ru.abramovkirill.languageapp.exercises.guess_animal.impl.ui.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import ru.abramovkirill.languageapp.core.navigation.compose_impl.Route
import ru.abramovkirill.languageapp.exercises.guess_animal.api.ui.navigation.GUESS_ANIMAL_SCREEN_ROUTE_PATH
import ru.abramovkirill.languageapp.exercises.guess_animal.impl.di.GuessAnimalComponent
import ru.abramovkirill.languageapp.exercises.guess_animal.impl.ui.screens.guess_animal.GuessAnimalScreen

object GuessAnimalScreenRoute : Route.Screen(
    path = GUESS_ANIMAL_SCREEN_ROUTE_PATH,
) {
    @Composable
    override fun AnimatedContentScope.Content(
        navController: NavController,
        navBackStackEntry: NavBackStackEntry
    ) {
        GuessAnimalScreen(
            viewModel = viewModel(
                factory = GuessAnimalComponent.guessAnimalViewModelFactory
            )
        )
    }
}
