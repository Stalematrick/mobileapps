package ru.abramovkirill.languageapp.audition.word_practice.impl.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import ru.abramovkirill.languageapp.audition.word_practice.impl.data.speech.AuditionSpeechRecognizerImpl
import ru.abramovkirill.languageapp.audition.word_practice.impl.domain.AuditionUseCase
import ru.abramovkirill.languageapp.audition.word_practice.impl.domain.speech.AuditionSpeechRecognizer
import ru.abramovkirill.languageapp.audition.word_practice.impl.ui.screens.word_practice.AuditionViewModel
import ru.abramovkirill.languageapp.core.coroutines.dispatchers.di.MainCoroutineDispatcherQualifier

internal val AuditionViewModelFactoryQualifier =
    qualifier("AuditionViewModelFactory")

val auditionModule = module {
    factory<AuditionUseCase> {
        AuditionUseCase(
            profilesRepository = get(),
        )
    }

    factory<AuditionSpeechRecognizer> {
        AuditionSpeechRecognizerImpl(
            applicationContext = get(),
            recognizerDispatcher = get(MainCoroutineDispatcherQualifier)
        )
    }

    single<ViewModelProvider.Factory>(AuditionViewModelFactoryQualifier) {
        viewModelFactory {
            initializer {
                AuditionViewModel(
                    router = get(),
                    wordPractice = get(),
                    speechRecognizer = get(),
                )
            }
        }
    }
}
