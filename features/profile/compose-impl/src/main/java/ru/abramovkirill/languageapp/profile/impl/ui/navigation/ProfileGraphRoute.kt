package ru.abramovkirill.languageapp.profile.impl.ui.navigation

import ru.abramovkirill.languageapp.core.navigation.compose_impl.Route
import ru.abramovkirill.languageapp.core.navigation.compose_impl.route
import ru.abramovkirill.languageapp.profile.api.ui.navigation.PROFILE_GRAPH_ROUTE_PATH

object ProfileGraphRoute : Route.Graph(
    path = PROFILE_GRAPH_ROUTE_PATH,
    startDestination = PROFILE_SCREEN_ROUTE_PATH,
    builder = {
        route(ProfileScreenRoute)
        route(SelectLanguageScreenRoute)
        route(AvatarCropScreenRoute)
    },
)
