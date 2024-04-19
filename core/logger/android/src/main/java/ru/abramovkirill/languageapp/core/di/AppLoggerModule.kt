package ru.abramovkirill.languageapp.core.di

import org.koin.dsl.module
import ru.abramovkirill.languageapp.core.AppLogger
import ru.abramovkirill.languageapp.core.AppLoggerAndroid

val androidLoggerModule = module {
    single<AppLogger> { AppLoggerAndroid() }
}
