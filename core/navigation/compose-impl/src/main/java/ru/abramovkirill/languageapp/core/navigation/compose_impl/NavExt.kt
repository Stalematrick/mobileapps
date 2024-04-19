@file:OptIn(ExperimentalAnimationApi::class)

package ru.abramovkirill.languageapp.core.navigation.compose_impl

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.abramovkirill.languageapp.core.design.composables.scaffold.LocalAppScaffoldController

fun NavGraphBuilder.route(route: Route) {
    when (route) {
        is Route.Graph -> {
            androidBuilder.navigation(
                startDestination = route.startDestination,
                route = route.path,
                builder = { applyLibraryBuilder(navController, route.builder) },
            )
        }
        is Route.Screen -> {
            androidBuilder.composable(
                route = route.pathWithParams,
                arguments = route.namedNavArguments,
                content = { navBackStackEntry ->
                    val appScaffoldController = LocalAppScaffoldController.current

                    appScaffoldController.isNavigationTransitionRunning =
                        transition.isRunning

                    with(route) {
                        Content(navController, navBackStackEntry)
                    }
                },
            )
        }
    }
}

@Composable
fun NavHost(
    navController: NavHostController,
    startDestination: Route,
    modifier: Modifier = Modifier,
    enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) =
        { fadeIn(animationSpec = tween(DEFAULT_NAVIGATION_ANIMATION_DURATION)) },
    exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) =
        { fadeOut(animationSpec = tween(DEFAULT_NAVIGATION_ANIMATION_DURATION)) },
    popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) =
        enterTransition,
    popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) =
        exitTransition,
    builder: NavGraphBuilder.() -> Unit,
) {
    androidx.navigation.compose.NavHost(
        navController = navController,
        startDestination = startDestination.path,
        modifier = modifier,
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popEnterTransition = popEnterTransition,
        popExitTransition = popExitTransition,
        builder = { applyLibraryBuilder(navController, builder) },
    )
}

@Composable
fun NavHost(
    navController: NavHostController,
    graph: Route.Graph,
    modifier: Modifier = Modifier,
    enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) =
        { fadeIn(animationSpec = tween(DEFAULT_NAVIGATION_ANIMATION_DURATION)) },
    exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) =
        { fadeOut(animationSpec = tween(DEFAULT_NAVIGATION_ANIMATION_DURATION)) },
    popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) =
        enterTransition,
    popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) =
        exitTransition,
) {
    androidx.navigation.compose.NavHost(
        navController = navController,
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popEnterTransition = popEnterTransition,
        popExitTransition = popExitTransition,
        graph = androidx.navigation.NavGraphBuilder(
            provider = navController.navigatorProvider,
            route = graph.path,
            startDestination = graph.startDestination,
        ).apply { applyLibraryBuilder(navController, graph.builder) }.build(),
        modifier = modifier,
    )
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavController,
    factory: ViewModelProvider.Factory? = null,
): T {
    val navGraphRoute = destination.parent?.route
        ?: return viewModel(factory = factory)

    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }

    return viewModel(viewModelStoreOwner = parentEntry, factory = factory)
}


private const val DEFAULT_NAVIGATION_ANIMATION_DURATION = 700
