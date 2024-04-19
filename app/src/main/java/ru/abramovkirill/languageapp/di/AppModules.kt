package ru.abramovkirill.languageapp.di

import ru.abramovkirill.languageapp.audition.word_practice.impl.di.auditionModule
import ru.abramovkirill.languageapp.auth.impl.di.authModule
import ru.abramovkirill.languageapp.core.coroutines.dispatchers.di.dispatchersModule
import ru.abramovkirill.languageapp.core.coroutines.scopes.di.coroutineScopesModule
import ru.abramovkirill.languageapp.core.data.http_client.di.httpClientModule
import ru.abramovkirill.languageapp.core.data.serialization.di.serializationModule
import ru.abramovkirill.languageapp.core.data.supabase.di.supabaseModule
import ru.abramovkirill.languageapp.core.di.androidLoggerModule
import ru.abramovkirill.languageapp.core.env.di.androidEnvironmentConfigModule
import ru.abramovkirill.languageapp.core.profiles.impl.di.profilesModule
import ru.abramovkirill.languageapp.exercises.guess_animal.impl.di.guessAnimalModule
import ru.abramovkirill.languageapp.exercises.word_practice.impl.di.wordPracticeModule
import ru.abramovkirill.languageapp.main.impl.di.mainModule
import ru.abramovkirill.languageapp.onboarding.impl.di.onboardingModule
import ru.abramovkirill.languageapp.profile.impl.di.profileModule

val appModules = listOf(
    appModule,
    coilModule,
    dispatchersModule,
    coroutineScopesModule,
    httpClientModule,
    serializationModule,
    supabaseModule,
    composeNavigationModule,
    profilesModule,
    androidLoggerModule,
    androidEnvironmentConfigModule,
    onboardingModule,
    authModule,
    mainModule,
    profileModule,
    guessAnimalModule,
    wordPracticeModule,
    auditionModule,
)
