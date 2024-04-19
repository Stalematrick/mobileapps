package ru.abramovkirill.languageapp.exercises.guess_animal.impl.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GuessAnimalExerciseAnswerData(
    @SerialName("id")
    val id: String,
    @SerialName("answer")
    val answer: String,
)
