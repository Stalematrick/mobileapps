@file:OptIn(KoinExperimentalAPI::class)

package ru.heat.languageapp.di

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.test.verify.verify
import ru.abramovkirill.languageapp.di.appModules

class DependencyGraphTest {
    @Test
    fun `check dependency graph`() {
        module {
            includes(appModules)
        }.verify(
            extraTypes = listOf(
                HttpClientEngine::class,
                HttpClientConfig::class,
            )
        )
    }
}
