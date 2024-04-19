package ru.abramovkirill.languageapp.core.profiles.impl.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ProfileData(
    @SerialName("id")
    val id: String,
    @SerialName("email")
    val email: String? = null,
    @SerialName("first_name")
    val firstName: String? = null,
    @SerialName("last_name")
    val lastName: String? = null,
    @SerialName("avatar_url")
    val avatarUrl: String? = null,
    @SerialName("total_score")
    val totalScore: Float = 0f,
)
