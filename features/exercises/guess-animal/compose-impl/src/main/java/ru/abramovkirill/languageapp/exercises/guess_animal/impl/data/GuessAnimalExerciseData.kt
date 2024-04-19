package ru.abramovkirill.languageapp.exercises.guess_animal.impl.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GuessAnimalExerciseData(
    @SerialName("id")
    val id: String,
    @SerialName("animal_image_url")
    val imageUrl: String,
)
