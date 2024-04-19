package ru.abramovkirill.languageapp.main.impl.di

import androidx.lifecycle.ViewModelProvider
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

object MainComponent : KoinComponent {
    val mainViewModelFactory: ViewModelProvider.Factory
        get() = get(MainViewModelFactoryQualifier)
}
