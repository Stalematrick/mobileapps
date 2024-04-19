package ru.abramovkirill.languageapp.di

import org.koin.dsl.module
import ru.abramovkirill.languageapp.core.navigation.api.Router
import ru.abramovkirill.languageapp.core.navigation.compose_impl.ComposeRouter

val composeNavigationModule = module {
    single<ComposeRouter> { ComposeRouter() }
    single<Router> { get<ComposeRouter>() }
}
