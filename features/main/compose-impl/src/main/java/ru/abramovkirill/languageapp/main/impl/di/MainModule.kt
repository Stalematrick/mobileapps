package ru.abramovkirill.languageapp.main.impl.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import ru.abramovkirill.languageapp.main.impl.ui.screens.main.MainViewModel

internal val MainViewModelFactoryQualifier = qualifier("MainViewModelFactory")

val mainModule = module {
    single<ViewModelProvider.Factory>(MainViewModelFactoryQualifier) {
        viewModelFactory {
            initializer {
                MainViewModel(
                    profilesRepository = get(),
                    settingsRepository = get(),
                    router = get(),
                )
            }
        }
    }
}
