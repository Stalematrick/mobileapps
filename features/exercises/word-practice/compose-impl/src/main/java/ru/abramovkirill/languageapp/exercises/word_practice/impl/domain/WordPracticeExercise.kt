package ru.abramovkirill.languageapp.exercises.word_practice.impl.domain

data class WordPracticeExercise(
    val id: String,
    val word: String,
    val wordTranscription: String? = null,
    val answers: List<String>,
)
