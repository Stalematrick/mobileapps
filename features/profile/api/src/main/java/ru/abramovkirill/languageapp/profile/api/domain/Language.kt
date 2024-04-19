package ru.abramovkirill.languageapp.profile.api.domain

enum class Language(val tag: String) {
    Russian("ru-RU"),
    English("en-US");

    companion object {
        val Default = Russian
    }
}
