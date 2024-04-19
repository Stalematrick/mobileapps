package ru.abramovkirill.languageapp.profile.impl.ui.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import ru.abramovkirill.languageapp.core.navigation.compose_impl.Route
import ru.abramovkirill.languageapp.core.navigation.compose_impl.sharedViewModel
import ru.abramovkirill.languageapp.profile.impl.di.ProfileComponent
import ru.abramovkirill.languageapp.profile.impl.ui.screens.profile.ProfileScreen

internal object ProfileScreenRoute : Route.Screen(
    path = PROFILE_SCREEN_ROUTE_PATH
) {
    @Composable
    override fun AnimatedContentScope.Content(
        navController: NavController,
        navBackStackEntry: NavBackStackEntry
    ) {
        ProfileScreen(
            viewModel = navBackStackEntry.sharedViewModel(
                navController = navController,
                factory = ProfileComponent.profileViewModelFactory,
            )
        )
    }
}

internal const val PROFILE_SCREEN_ROUTE_PATH = "profile"
