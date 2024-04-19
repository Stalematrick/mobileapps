package ru.abramovkirill.languageapp.exercises.word_practice.impl.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WordPracticeExerciseAnswerData(
    @SerialName("id")
    val id: String,
    @SerialName("answer")
    val answer: String,
)
