package ru.abramovkirill.languageapp.core.coroutines.scopes.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

val LongRunningCoroutineScopeQualifier = qualifier("LongRunningCoroutineScope")

val coroutineScopesModule = module {
    single<CoroutineScope>(LongRunningCoroutineScopeQualifier) { MainScope() }
}
