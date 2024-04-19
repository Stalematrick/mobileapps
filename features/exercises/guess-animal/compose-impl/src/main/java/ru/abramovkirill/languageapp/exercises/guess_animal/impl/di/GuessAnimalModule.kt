package ru.abramovkirill.languageapp.exercises.guess_animal.impl.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import ru.abramovkirill.languageapp.exercises.guess_animal.impl.domain.GuessAnimalUseCase
import ru.abramovkirill.languageapp.exercises.guess_animal.impl.ui.screens.guess_animal.GuessAnimalViewModel

internal val GuessAnimalViewModelFactoryQualifier =
    qualifier("GuessAnimalViewModelFactory")

val guessAnimalModule = module {
    factory<GuessAnimalUseCase> {
        GuessAnimalUseCase()
    }

    single<ViewModelProvider.Factory>(GuessAnimalViewModelFactoryQualifier) {
        viewModelFactory {
            initializer {
                GuessAnimalViewModel(
                    router = get()
                )
            }
        }
    }
}
