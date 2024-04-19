package ru.abramovkirill.languageapp.exercises.word_practice.impl.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import ru.abramovkirill.languageapp.exercises.word_practice.impl.domain.WordPracticeUseCase
import ru.abramovkirill.languageapp.exercises.word_practice.impl.ui.screens.word_practice.WordPracticeViewModel

internal val WordPracticeViewModelFactoryQualifier =
    qualifier("WordPracticeViewModelFactory")

val wordPracticeModule = module {

    factory<WordPracticeUseCase> {
        WordPracticeUseCase()
    }

    single<ViewModelProvider.Factory>(WordPracticeViewModelFactoryQualifier) {
        viewModelFactory {
            initializer {
                WordPracticeViewModel(
                    router = get(),
                    wordPractice = get(),
                )
            }
        }
    }
}
