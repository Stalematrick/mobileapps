package ru.abramovkirill.languageapp.core.navigation.api

import io.ktor.http.Url

interface DeepLinkRouter {
    suspend fun handle(data: Url): Boolean
}
