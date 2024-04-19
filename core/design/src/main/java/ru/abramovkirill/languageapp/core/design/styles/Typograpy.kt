package ru.abramovkirill.languageapp.core.design.styles

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import ru.abramovkirill.languageapp.core.design.R

private val defaultTypography = Typography()

private val appFont = FontFamily(
    Font(R.font.fredoka_light, weight = FontWeight.Light, style = FontStyle.Normal),
    Font(R.font.fredoka_regular, weight = FontWeight.Normal, style = FontStyle.Normal),
    Font(R.font.fredoka_medium, weight = FontWeight.Medium, style = FontStyle.Normal),
    Font(R.font.fredoka_semibold, weight = FontWeight.SemiBold, style = FontStyle.Normal),
    Font(R.font.fredoka_bold, weight = FontWeight.Bold, style = FontStyle.Normal),
)

val appTypography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = appFont),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = appFont),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = appFont),

    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = appFont),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = appFont),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = appFont),

    titleLarge = defaultTypography.titleLarge.copy(fontFamily = appFont),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = appFont),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = appFont),

    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = appFont),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = appFont),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = appFont),

    labelLarge = defaultTypography.labelLarge.copy(fontFamily = appFont),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = appFont),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = appFont)
)
