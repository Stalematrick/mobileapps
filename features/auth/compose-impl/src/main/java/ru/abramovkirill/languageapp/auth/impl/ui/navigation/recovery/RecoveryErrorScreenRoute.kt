package ru.abramovkirill.languageapp.auth.impl.ui.navigation.recovery

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import ru.abramovkirill.languageapp.auth.impl.ui.screens.recovery.RecoveryFlowViewModel
import ru.abramovkirill.languageapp.auth.impl.ui.screens.recovery.error.RecoveryErrorScreen
import ru.abramovkirill.languageapp.core.navigation.compose_impl.Route

internal class RecoveryErrorScreenRoute(
    private val recoveryViewModelProvider: () -> RecoveryFlowViewModel,
) : Route.Screen(
    path = RECOVERY_ERROR_SCREEN_ROUTE_PATH
) {
    @Composable
    override fun AnimatedContentScope.Content(
        navController: NavController,
        navBackStackEntry: NavBackStackEntry
    ) {
        RecoveryErrorScreen(viewModel = recoveryViewModelProvider())
    }
}

internal const val RECOVERY_ERROR_SCREEN_ROUTE_PATH = "recovery_error"
