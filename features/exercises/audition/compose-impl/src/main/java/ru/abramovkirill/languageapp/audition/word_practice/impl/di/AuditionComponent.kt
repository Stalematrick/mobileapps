package ru.abramovkirill.languageapp.audition.word_practice.impl.di

import androidx.lifecycle.ViewModelProvider
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

object AuditionComponent : KoinComponent {
    val wordPracticeViewModelFactory: ViewModelProvider.Factory
        get() = get(AuditionViewModelFactoryQualifier)
}
