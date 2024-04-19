package ru.abramovkirill.languageapp.exercises.word_practice.impl.ui.screens.word_practice

import androidx.compose.runtime.Immutable

@Immutable
data class WordPracticeAnswerItem(
    val word: String,
    val selectionType: WordPracticeAnswerSelectionType =
        WordPracticeAnswerSelectionType.None,
)

enum class WordPracticeAnswerSelectionType {
    User,
    Correct,
    Incorrect,
    None;
}
