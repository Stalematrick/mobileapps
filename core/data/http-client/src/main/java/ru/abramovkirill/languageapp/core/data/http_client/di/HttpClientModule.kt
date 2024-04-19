package ru.abramovkirill.languageapp.core.data.http_client.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import org.koin.dsl.module
import ru.abramovkirill.languageapp.core.AppLogger

val httpClientModule = module {
    single<HttpClient> {
        HttpClient {
            // config ....
            expectSuccess = true

            // plugins ....
            install(Logging) {
                logger = object: Logger {
                    val logger = get<AppLogger>()

                    override fun log(message: String) {
                        logger.d("HTTP Client", message)
                    }
                }
                level = LogLevel.ALL
            }

            install(ContentNegotiation) {
                json(get())
            }
        }
    }
}
