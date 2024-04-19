package ru.abramovkirill.languageapp.core.navigation.api

sealed interface RoutingOption {
    data class LaunchSingleTop(val launchSingleTop: Boolean): RoutingOption
    data class PopUpTo(val routePath: String, val inclusive: Boolean = false) : RoutingOption
    data object ClearStack : RoutingOption
}
