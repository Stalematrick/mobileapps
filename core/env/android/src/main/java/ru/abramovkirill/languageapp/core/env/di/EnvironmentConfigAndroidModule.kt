package ru.abramovkirill.languageapp.core.env.di

import org.koin.dsl.module
import ru.abramovkirill.languageapp.core.env.EnvironmentConfig
import ru.abramovkirill.languageapp.core.env.EnvironmentConfigAndroid

val androidEnvironmentConfigModule = module {
    single<EnvironmentConfig> { EnvironmentConfigAndroid() }
}
