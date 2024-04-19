package ru.abramovkirill.languageapp.core.design.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavBackStackEntry

private typealias NavigationTransitionScope = AnimatedContentTransitionScope<NavBackStackEntry>

private const val TRANSITION_DURATION = 300

object NavigationTransitions  {
    val enter: NavigationTransitionScope.() -> EnterTransition =
        {
            slideInHorizontally(
                animationSpec = tween(durationMillis = TRANSITION_DURATION),
                initialOffsetX = { fullWidth -> fullWidth * 2 },
            )
        }

    val exit: NavigationTransitionScope.() -> ExitTransition =
        {
            slideOutHorizontally(
                animationSpec = tween(durationMillis = TRANSITION_DURATION),
                targetOffsetX = { fullWidth -> -fullWidth },
            )
        }

    val popEnter: NavigationTransitionScope.() -> EnterTransition =
        {
            slideInHorizontally(
                animationSpec = tween(durationMillis = TRANSITION_DURATION),
                initialOffsetX = { fullWidth -> -fullWidth },
            )
        }

    val popExit: NavigationTransitionScope.() -> ExitTransition =
        {
            slideOutHorizontally(
                animationSpec = tween(durationMillis = TRANSITION_DURATION),
                targetOffsetX = { fullWidth -> fullWidth * 2 },
            )
        }
}
