package ru.abramovkirill.languageapp.core.profiles.api.domain

data class Profile(
    val id: String,
    val email: String?,
    val firstName: String?,
    val lastName: String?,
    val totalScore: Float,
    val avatarUrl: String?,
)
