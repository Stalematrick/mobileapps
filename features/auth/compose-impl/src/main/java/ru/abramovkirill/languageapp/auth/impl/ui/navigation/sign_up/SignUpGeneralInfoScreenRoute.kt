package ru.abramovkirill.languageapp.auth.impl.ui.navigation.sign_up

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import ru.abramovkirill.languageapp.auth.impl.ui.screens.sign_up.SignUpViewModel
import ru.abramovkirill.languageapp.auth.impl.ui.screens.sign_up.general_info.SignUpGeneralInfoScreen
import ru.abramovkirill.languageapp.core.navigation.compose_impl.Route

internal class SignUpGeneralInfoScreenRoute(
    private val signUpViewModelProvider: () -> SignUpViewModel,
) : Route.Screen(
    path = SIGN_UP_GENERAL_INFO_SCREEN_ROUTE_PATH
) {
    @Composable
    override fun AnimatedContentScope.Content(
        navController: NavController,
        navBackStackEntry: NavBackStackEntry
    ) {
        SignUpGeneralInfoScreen(viewModel = signUpViewModelProvider())
    }
}

internal const val SIGN_UP_GENERAL_INFO_SCREEN_ROUTE_PATH = "sign_up_general_info"
