package ru.abramovkirill.languageapp.audition.word_practice.impl.domain

data class AuditionExercise(
    val id: String,
    val word: String,
    val wordTranscription: String? = null,
)
