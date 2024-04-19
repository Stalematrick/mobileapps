package ru.abramovkirill.languageapp.profile.impl.ui.screens.select_language

import ru.abramovkirill.languageapp.common.utils.StringResource

internal data class LanguageItem(
    val id: String,
    val name: StringResource,
    val isSelected: Boolean = false,
)
