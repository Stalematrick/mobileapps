package ru.abramovkirill.languageapp.exercises.guess_animal.impl.di

import androidx.lifecycle.ViewModelProvider
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

object GuessAnimalComponent : KoinComponent {
    val guessAnimalViewModelFactory: ViewModelProvider.Factory
        get() = get(GuessAnimalViewModelFactoryQualifier)
}
