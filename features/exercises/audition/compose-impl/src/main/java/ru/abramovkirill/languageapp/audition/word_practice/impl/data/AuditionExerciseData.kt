package ru.abramovkirill.languageapp.audition.word_practice.impl.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuditionExerciseData(
    @SerialName("id")
    val id: String,
    @SerialName("word")
    val word: String,
    @SerialName("word_transcription")
    val wordTranscription: String? = null,
)
