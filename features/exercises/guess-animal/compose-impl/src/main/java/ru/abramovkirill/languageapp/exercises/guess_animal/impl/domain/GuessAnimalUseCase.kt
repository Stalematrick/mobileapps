package ru.abramovkirill.languageapp.exercises.guess_animal.impl.domain


internal class GuessAnimalUseCase(
) {
    data class Result(
        val isCorrect: Boolean,
        val correctAnswer: String,
    )
}
