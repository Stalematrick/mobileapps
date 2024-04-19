package ru.abramovkirill.languageapp.exercises.word_practice.impl.di

import androidx.lifecycle.ViewModelProvider
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

object WordPracticeComponent : KoinComponent {
    val wordPracticeViewModelFactory: ViewModelProvider.Factory
        get() = get(WordPracticeViewModelFactoryQualifier)
}
