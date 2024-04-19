package ru.abramovkirill.languageapp.audition.word_practice.impl.domain

import ru.abramovkirill.languageapp.core.profiles.api.domain.ProfilesRepository

internal class AuditionUseCase(
    private val profilesRepository: ProfilesRepository,
) {
    suspend operator fun invoke(
        streak: Int,
        correctAnswer: String,
        answer: String,
    ): Result {
        val isAnswerCorrect = answer.trim().equals(correctAnswer.trim(), ignoreCase = true)

        if (isAnswerCorrect) {
            val streakPoints = if (streak > 0) STREAK_MULTIPLIER * (streak + 1) else 0f

            profilesRepository
                .increaseProfileTotalPoints(
                    points = CORRECT_ANSWER_POINTS_COUNT + streakPoints
                )
        }

        return Result(
            isCorrect = isAnswerCorrect,
            correctAnswer = correctAnswer.trim()
        )
    }

    data class Result(
        val isCorrect: Boolean,
        val correctAnswer: String,
    )

    companion object {
        private const val CORRECT_ANSWER_POINTS_COUNT = 1f
        private const val STREAK_MULTIPLIER = 2f
    }
}
