package ru.abramovkirill.languageapp.auth.impl.ui.navigation.sign_up

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import ru.abramovkirill.languageapp.auth.impl.di.AuthComponent
import ru.abramovkirill.languageapp.auth.impl.ui.screens.sign_up.SignUpFlow
import ru.abramovkirill.languageapp.core.navigation.compose_impl.Route

internal object SignUpFlowRoute : Route.Screen(
    path = SIGN_UP_FLOW_ROUTE_PATH
) {
    @Composable
    override fun AnimatedContentScope.Content(
        navController: NavController,
        navBackStackEntry: NavBackStackEntry
    ) {
        SignUpFlow(
            viewModel = viewModel(
                factory = AuthComponent.signUpViewModelFactory
            )
        )
    }
}

internal const val SIGN_UP_FLOW_ROUTE_PATH = "sign_up"
