package ru.abramovkirill.languageapp.auth.impl.ui.navigation.sign_up

import ru.abramovkirill.languageapp.auth.impl.ui.screens.sign_up.SignUpViewModel
import ru.abramovkirill.languageapp.core.navigation.compose_impl.Route
import ru.abramovkirill.languageapp.core.navigation.compose_impl.route

internal class SignUpGraphRoute(
    private val signUpViewModelProvider: () -> SignUpViewModel,
) : Route.Graph(
    path = SIGN_UP_GRAPH_ROUTE_PATH,
    startDestination = SIGN_UP_GENERAL_INFO_SCREEN_ROUTE_PATH,
    builder = {
        route(SignUpGeneralInfoScreenRoute(signUpViewModelProvider))
        route(SignUpPasswordScreenRoute(signUpViewModelProvider))
        route(SignUpEmailConfirmScreenRoute(signUpViewModelProvider))
    }
)

internal const val SIGN_UP_GRAPH_ROUTE_PATH = "sign_up_graph"
