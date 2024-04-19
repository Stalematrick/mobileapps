package ru.abramovkirill.languageapp.core.coroutines.dispatchers.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

val MainCoroutineDispatcherQualifier = qualifier("MainCoroutineDispatcher")
val IoCoroutineDispatcherQualifier = qualifier("IoCoroutineDispatcher")
val DefaultCoroutineDispatcherQualifier = qualifier("DefaultCoroutineDispatcher")

val dispatchersModule = module {
    single<CoroutineDispatcher>(MainCoroutineDispatcherQualifier) { Dispatchers.Main }
    single<CoroutineDispatcher>(IoCoroutineDispatcherQualifier) { Dispatchers.IO }
    single<CoroutineDispatcher>(DefaultCoroutineDispatcherQualifier) { Dispatchers.Default }
}
