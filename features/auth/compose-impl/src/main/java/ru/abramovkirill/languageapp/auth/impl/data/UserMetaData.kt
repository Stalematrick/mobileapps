package ru.abramovkirill.languageapp.auth.impl.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class UserMetaData(
    @SerialName("first_name")
    val firstName: String? = null,
    @SerialName("last_name")
    val lastName: String? = null,
    @SerialName("avatar_url")
    val avatarUrl: String? = null,
)
