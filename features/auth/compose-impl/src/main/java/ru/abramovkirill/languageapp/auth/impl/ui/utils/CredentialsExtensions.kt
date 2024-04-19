package ru.abramovkirill.languageapp.auth.impl.ui.utils

import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException

fun GetCredentialException.isNotFatal() =
    this is GetCredentialCancellationException

fun GetCredentialException.isFatal() =
    !isNotFatal()
