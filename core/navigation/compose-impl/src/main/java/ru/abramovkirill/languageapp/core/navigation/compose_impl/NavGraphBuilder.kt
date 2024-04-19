package ru.abramovkirill.languageapp.core.navigation.compose_impl

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

data class NavGraphBuilder(
    val androidBuilder: NavGraphBuilder,
    val navController: NavController,
)

fun NavGraphBuilder.applyLibraryBuilder(
    navController: NavController,
    builder: ru.abramovkirill.languageapp.core.navigation.compose_impl.NavGraphBuilder.() -> Unit,
) {
    builder(
        NavGraphBuilder(
            androidBuilder = this,
            navController = navController
        )
    )
}
