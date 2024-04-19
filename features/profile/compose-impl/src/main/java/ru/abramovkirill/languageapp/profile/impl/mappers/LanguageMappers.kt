package ru.abramovkirill.languageapp.profile.impl.mappers

import ru.abramovkirill.languageapp.common.utils.strRes
import ru.abramovkirill.languageapp.profile.api.domain.Language
import ru.abramovkirill.languageapp.profile.impl.R
import ru.abramovkirill.languageapp.profile.impl.ui.screens.select_language.LanguageItem
import java.util.Locale

internal fun Locale.toLanguage() =
    Language.entries.find { language -> language.tag == toLanguageTag() }

internal fun Language.toUiItem() = LanguageItem(
    id = name,
    name = when (this) {
        Language.Russian -> strRes(R.string.language_select_russian)
        Language.English -> strRes(R.string.language_select_english)
    },
    isSelected = Language.Default == this,
)

internal fun LanguageItem.toDomain() =
    Language.entries.find { language -> language.name == id }
