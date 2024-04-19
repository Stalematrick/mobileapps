package ru.abramovkirill.languageapp.di

import android.app.Application
import coil.ImageLoader
import coil.imageLoader
import org.koin.dsl.module

val coilModule = module {
    single<ImageLoader> {
        get<Application>().imageLoader
    }
}
