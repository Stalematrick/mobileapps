package ru.abramovkirill.languageapp.common.utils

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController

val NavController.currentRoute
    get() = currentBackStackEntry?.destination?.route

val NavController.previousRoute
    get() = previousBackStackEntry?.destination?.route

val NavController.parentNavGraph
    get() = currentBackStackEntry?.destination?.parent

fun NavBackStackEntry.requireLongArg(key: String) = requireNotNull(arguments?.getLong(key))

fun NavBackStackEntry.requireStringArg(key: String) = requireNotNull(arguments?.getString(key))
