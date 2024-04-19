package ru.abramovkirill.languageapp.exercises.word_practice.impl.domain


internal class WordPracticeUseCase() {

    private val isCorrect: Boolean = true

    suspend operator fun invoke(
        streak: Int,
        exerciseId: String,
        answer: String,
    ): Result {
        val isAnswerCorrect = isCorrect

        if (isAnswerCorrect) {
            val streakPoints = if (streak > 0) STREAK_MULTIPLIER * (streak + 1) else 0f
        }

        return Result(
            isCorrect = isAnswerCorrect,
            correctAnswer = "answer"
        )
    }

    data class Result(
        val isCorrect: Boolean,
        val correctAnswer: String,
    )

    companion object {
        private const val CORRECT_ANSWER_POINTS_COUNT = 1f
        private const val STREAK_MULTIPLIER = 0.2f
    }
}
