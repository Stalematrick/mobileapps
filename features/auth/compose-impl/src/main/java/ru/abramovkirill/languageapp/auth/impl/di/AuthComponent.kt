package ru.abramovkirill.languageapp.auth.impl.di

import androidx.lifecycle.ViewModelProvider
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import ru.abramovkirill.languageapp.core.env.EnvironmentConfig
import ru.abramovkirill.languageapp.core.navigation.compose_impl.ComposeRouter

object AuthComponent : KoinComponent {

    val environmentConfig: EnvironmentConfig by inject()

    val signInViewModelFactory: ViewModelProvider.Factory
        get() = get(SignInViewModelFactoryQualifier)

    val signUpViewModelFactory: ViewModelProvider.Factory
        get() = get(SignUpViewModelFactoryQualifier)

    val recoveryViewModelFactory: ViewModelProvider.Factory
        get() = get(RecoveryViewModelFactoryQualifier)

    val signUpComposeRouter: ComposeRouter
        get() = get(SignUpComposeRouterQualifier)

    val recoveryRouter: ComposeRouter
        get() = get(RecoveryComposeRouterQualifier)
}
