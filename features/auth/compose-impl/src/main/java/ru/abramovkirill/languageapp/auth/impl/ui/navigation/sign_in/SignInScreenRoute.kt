package ru.abramovkirill.languageapp.auth.impl.ui.navigation.sign_in

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import ru.abramovkirill.languageapp.auth.impl.di.AuthComponent
import ru.abramovkirill.languageapp.auth.impl.ui.screens.sign_in.SignInScreen
import ru.abramovkirill.languageapp.core.navigation.compose_impl.Route

internal object SignInScreenRoute : Route.Screen(
    path =  SIGN_IN_SCREEN_ROUTE_PATH
) {
    @Composable
    override fun AnimatedContentScope.Content(
        navController: NavController,
        navBackStackEntry: NavBackStackEntry
    ) {
        SignInScreen(
            viewModel = viewModel(
                factory = AuthComponent.signInViewModelFactory
            )
        )
    }
}

internal const val SIGN_IN_SCREEN_ROUTE_PATH = "sign_in"
