package ru.abramovkirill.languageapp.di

import android.app.Application
import org.koin.dsl.module
import ru.abramovkirill.languageapp.LanguageApplication

val appModule = module {
    single<Application> { LanguageApplication.instance }
}
