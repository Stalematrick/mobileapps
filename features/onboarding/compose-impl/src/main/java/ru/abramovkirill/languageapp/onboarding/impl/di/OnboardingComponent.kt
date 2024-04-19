package ru.abramovkirill.languageapp.onboarding.impl.di

import androidx.lifecycle.ViewModelProvider
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

object OnboardingComponent : KoinComponent {
    val onboardingViewModelFactory: ViewModelProvider.Factory
        get() = get(OnboardingViewModelFactoryQualifier)
}
