package ru.abramovkirill.languageapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import ru.abramovkirill.languageapp.audition.word_practice.impl.ui.navigation.AuditionScreenRoute
import ru.abramovkirill.languageapp.auth.impl.ui.navigation.AuthGraphRoute
import ru.abramovkirill.languageapp.core.design.navigation.NavigationTransitions
import ru.abramovkirill.languageapp.core.navigation.compose_impl.NavGraphBuilder
import ru.abramovkirill.languageapp.core.navigation.compose_impl.NavHost
import ru.abramovkirill.languageapp.core.navigation.compose_impl.route
import ru.abramovkirill.languageapp.exercises.guess_animal.impl.ui.navigation.GuessAnimalScreenRoute
import ru.abramovkirill.languageapp.exercises.word_practice.impl.ui.navigation.WordPracticeScreenRoute
import ru.abramovkirill.languageapp.features.splash.impl.ui.navigation.SplashScreenRoute
import ru.abramovkirill.languageapp.main.impl.ui.navigation.MainScreenRoute
import ru.abramovkirill.languageapp.onboarding.impl.ui.navigation.OnboardingScreenRoute
import ru.abramovkirill.languageapp.profile.impl.ui.navigation.ProfileGraphRoute
import ru.abramovkirill.languageapp.profile.impl.ui.navigation.SelectLanguageScreenRoute

private fun NavGraphBuilder.buildGraph() {
    route(SplashScreenRoute)
    route(OnboardingScreenRoute)
    route(AuthGraphRoute)
    route(SelectLanguageScreenRoute)
    route(MainScreenRoute)
    route(ProfileGraphRoute)
    route(GuessAnimalScreenRoute)
    route(WordPracticeScreenRoute)
    route(AuditionScreenRoute)
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = SplashScreenRoute,
        enterTransition = NavigationTransitions.enter,
        exitTransition = NavigationTransitions.exit,
        popEnterTransition = NavigationTransitions.popEnter,
        popExitTransition = NavigationTransitions.popExit,
        builder = NavGraphBuilder::buildGraph,
        modifier = modifier,
    )
}
