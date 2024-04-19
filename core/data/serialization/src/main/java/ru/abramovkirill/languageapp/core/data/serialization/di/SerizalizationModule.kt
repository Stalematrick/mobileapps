package ru.abramovkirill.languageapp.core.data.serialization.di

import kotlinx.serialization.json.Json
import org.koin.dsl.module

val serializationModule = module {
    single<Json> {
        Json {
            encodeDefaults = true
            coerceInputValues = true
            ignoreUnknownKeys = true
        }
    }
}
