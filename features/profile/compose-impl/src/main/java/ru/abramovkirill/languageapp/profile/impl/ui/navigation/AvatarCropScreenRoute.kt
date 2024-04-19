package ru.abramovkirill.languageapp.profile.impl.ui.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import ru.abramovkirill.languageapp.common.utils.requireStringArg
import ru.abramovkirill.languageapp.core.navigation.compose_impl.Route
import ru.abramovkirill.languageapp.profile.impl.di.ProfileComponent
import ru.abramovkirill.languageapp.profile.impl.ui.screens.avatar_crop.AvatarCropScreen

internal object AvatarCropScreenRoute : Route.Screen(
    path = AVATAR_CROP_SCREEN_ROUTE_PATH,
    namedNavArguments = listOf(
        navArgument(AvatarCropScreenArguments.PHOTO_URI) {
            type = NavType.StringType
        }
    )
) {
    @Composable
    override fun AnimatedContentScope.Content(
        navController: NavController,
        navBackStackEntry: NavBackStackEntry
    ) {
        val photoUri = navBackStackEntry.requireStringArg(AvatarCropScreenArguments.PHOTO_URI)

        AvatarCropScreen(
            viewModel = viewModel(
                factory = ProfileComponent.getAvatarCropViewModelFactory(
                    photoUri = photoUri
                )
            )
        )
    }
}

object AvatarCropScreenArguments {
    const val PHOTO_URI = "photo_uri"
}

internal const val AVATAR_CROP_SCREEN_ROUTE_PATH = "avatar_crop"
