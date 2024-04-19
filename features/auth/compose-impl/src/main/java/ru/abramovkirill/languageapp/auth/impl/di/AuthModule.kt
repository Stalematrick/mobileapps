package ru.abramovkirill.languageapp.auth.impl.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.koin.core.module.Module
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import ru.abramovkirill.languageapp.auth.api.domain.AuthSignOutUseCase
import ru.abramovkirill.languageapp.auth.api.domain.AuthRepository
import ru.abramovkirill.languageapp.auth.api.domain.google.AuthGoogleNonceProvider
import ru.abramovkirill.languageapp.auth.impl.data.AuthRepositoryImpl
import ru.abramovkirill.languageapp.auth.impl.data.AuthStorage
import ru.abramovkirill.languageapp.auth.impl.data.google.AuthGoogleNonceProviderImpl
import ru.abramovkirill.languageapp.auth.impl.domain.AuthSignOutUseCaseImpl
import ru.abramovkirill.languageapp.auth.impl.domain.password_validator.PasswordValidator
import ru.abramovkirill.languageapp.auth.impl.domain.recovery.RecoveryUseCase
import ru.abramovkirill.languageapp.auth.impl.domain.sign_in.SignInUseCase
import ru.abramovkirill.languageapp.auth.impl.domain.sign_in.SignInWithGoogleUseCase
import ru.abramovkirill.languageapp.auth.impl.domain.sign_up.SignUpUseCase
import ru.abramovkirill.languageapp.auth.impl.ui.navigation.AuthDeepLinkRouter
import ru.abramovkirill.languageapp.auth.impl.ui.screens.recovery.RecoveryFlowViewModel
import ru.abramovkirill.languageapp.auth.impl.ui.screens.sign_in.SignInViewModel
import ru.abramovkirill.languageapp.auth.impl.ui.screens.sign_up.SignUpViewModel
import ru.abramovkirill.languageapp.core.coroutines.dispatchers.di.DefaultCoroutineDispatcherQualifier
import ru.abramovkirill.languageapp.core.coroutines.dispatchers.di.IoCoroutineDispatcherQualifier
import ru.abramovkirill.languageapp.core.navigation.api.DeepLinkRouter
import ru.abramovkirill.languageapp.core.navigation.api.Router
import ru.abramovkirill.languageapp.core.navigation.compose_impl.ComposeRouter

internal val RecoveryRouterQualifier = qualifier("RecoveryRouter")
internal val RecoveryComposeRouterQualifier = qualifier("RecoveryComposeRouter")
internal val SignUpRouterQualifier = qualifier("SignUpRouter")
internal val SignUpComposeRouterQualifier = qualifier("SignUpComposeRouter")
internal val SignInViewModelFactoryQualifier = qualifier("SignInViewModelFactory")
internal val SignUpViewModelFactoryQualifier = qualifier("SignUpViewModelFactory")
internal val RecoveryViewModelFactoryQualifier = qualifier("RecoveryViewModelFactory")

val authModule = module {
    useDataBeans()
    useUseCasesBeans()
    useViewModelFactoriesBeans()
    useAuthBeans()
    useNavigationBeans()
}

private fun Module.useNavigationBeans() {
    single<ComposeRouter>(RecoveryComposeRouterQualifier) { ComposeRouter() }
    single<Router>(RecoveryRouterQualifier) { get<ComposeRouter>(RecoveryComposeRouterQualifier) }

    single<ComposeRouter>(SignUpComposeRouterQualifier) { ComposeRouter() }
    single<Router>(SignUpRouterQualifier) { get<ComposeRouter>(SignUpComposeRouterQualifier) }

    single<DeepLinkRouter> {
        AuthDeepLinkRouter(
            router = get(),
            recoveryRouter = get(RecoveryRouterQualifier),
            authRepository = get()
        )
    }
}

private fun Module.useAuthBeans() {
    factory<AuthGoogleNonceProvider> {
        AuthGoogleNonceProviderImpl(
            dispatcher = get(DefaultCoroutineDispatcherQualifier),
        )
    }
}

private fun Module.useViewModelFactoriesBeans() {
    single<ViewModelProvider.Factory>(SignInViewModelFactoryQualifier) {
        viewModelFactory {
            initializer {
                SignInViewModel(
                    signIn = get(),
                    signInWithGoogle = get(),
                    authGoogleNonceProvider = get(),
                    router = get(),
                )
            }
        }
    }

    single<ViewModelProvider.Factory>(SignUpViewModelFactoryQualifier) {
        viewModelFactory {
            initializer {
                SignUpViewModel(
                    router = get(),
                    signUpRouter = get(SignUpRouterQualifier),
                    signUp = get()
                )
            }
        }
    }

    single<ViewModelProvider.Factory>(RecoveryViewModelFactoryQualifier) {
        viewModelFactory {
            initializer {
                RecoveryFlowViewModel(
                    resetPassword = get(),
                    router = get(),
                    recoveryRouter = get(RecoveryRouterQualifier)
                )
            }
        }
    }
}

private fun Module.useUseCasesBeans() {
    factory<PasswordValidator> { PasswordValidator() }

    factory<SignInUseCase> {
        SignInUseCase(
            authRepository = get(),
            profilesRepository = get(),
        )
    }

    factory<SignInWithGoogleUseCase> {
        SignInWithGoogleUseCase(
            authRepository = get(),
            profilesRepository = get(),
        )
    }

    factory<SignUpUseCase> {
        SignUpUseCase(
            repository = get(),
            validatePassword = get()
        )
    }

    factory<RecoveryUseCase> {
        RecoveryUseCase(
            repository = get(),
            validatePassword = get()
        )
    }

    factory<AuthSignOutUseCase> {
        AuthSignOutUseCaseImpl(
            authRepository = get(),
            profilesRepository = get(),
        )
    }
}

private fun Module.useDataBeans() {
    single<AuthStorage> {
        AuthStorage(
            storageDispatcher = get(IoCoroutineDispatcherQualifier),
            applicationContext = get()
        )
    }

    single<AuthRepository> {
        AuthRepositoryImpl(
            supabaseClient = get(),
            authStorage = get(),
            json = get(),
            supabaseDispatcher = get(IoCoroutineDispatcherQualifier),
            environmentConfig = get(),
        )
    }
}
