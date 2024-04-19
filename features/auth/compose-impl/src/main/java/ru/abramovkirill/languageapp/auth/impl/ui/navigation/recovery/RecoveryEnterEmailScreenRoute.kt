package ru.abramovkirill.languageapp.auth.impl.ui.navigation.recovery

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import ru.abramovkirill.languageapp.auth.impl.ui.screens.recovery.RecoveryFlowViewModel
import ru.abramovkirill.languageapp.auth.impl.ui.screens.recovery.enter_email.RecoveryEnterEmailScreen
import ru.abramovkirill.languageapp.core.navigation.compose_impl.Route

internal class RecoveryEnterEmailScreenRoute(
    private val recoveryViewModelProvider: () -> RecoveryFlowViewModel,
) : Route.Screen(
    path = RECOVERY_ENTER_EMAIL_SCREEN_ROUTE_PATH
) {
    @Composable
    override fun AnimatedContentScope.Content(
        navController: NavController,
        navBackStackEntry: NavBackStackEntry
    ) {
        RecoveryEnterEmailScreen(viewModel = recoveryViewModelProvider())
    }
}

internal const val RECOVERY_ENTER_EMAIL_SCREEN_ROUTE_PATH = "recovery_enter_email"
