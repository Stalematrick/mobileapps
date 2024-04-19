package ru.abramovkirill.languageapp.exercises.word_practice.impl.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WordPracticeExerciseData(
    @SerialName("id")
    val id: String,
    @SerialName("word")
    val word: String,
    @SerialName("word_transcription")
    val wordTranscription: String? = null,
    @SerialName("answers")
    val answers: List<String> = emptyList(),
)
