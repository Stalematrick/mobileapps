package ru.abramovkirill.languageapp.profile.impl.domain

sealed interface AvatarCrop {
    data object Full : AvatarCrop

    data class Exactly(
        val left: Int,
        val top: Int,
        val width: Int,
        val height: Int
    ) : AvatarCrop
}
