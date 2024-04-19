package ru.abramovkirill.languageapp.core.navigation.compose_test

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import ru.abramovkirill.languageapp.core.navigation.compose_impl.ComposeRouter

@Composable
fun rememberTestComposeRouter(
    navController: NavController
): ComposeRouter {
    val router = remember { ComposeRouter() }
    val coroutineScope = rememberCoroutineScope()

    DisposableEffect(navController, router) {
        router.attachNavController(
            navController = navController,
            coroutineScope = coroutineScope,
        )

        onDispose { router.detachNavController() }
    }

    return router
}
