package ru.abramovkirill.languageapp.core.design.utils

import ru.abramovkirill.languageapp.common.utils.strRes
import ru.abramovkirill.languageapp.core.design.R

fun formatFullName(firstName: String?, lastName: String?) =
    when {
        firstName == null && lastName == null ->
            null
        firstName == null ->
            strRes(lastName)
        lastName == null ->
            strRes(firstName)
        else ->
            strRes(R.string.full_name_formatted, firstName, lastName)
    }
