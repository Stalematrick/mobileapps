@file:Suppress("MagicNumber")

package ru.abramovkirill.languageapp.core.design.styles

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class AppColors(
    val materialColors: ColorScheme,
    val shimmerBackground: Color,
    val shimmerForeground: Color,
    val progressBackground: Color,
    val inputFieldBackground: Color,
    val inputFieldErrorBackground: Color,
    val inputFieldHint: Color,
    val inputFieldText: Color,
    val textBody: Color,
    val success: Color,
    val onSuccess: Color,
    val exerciseGuessAnimal: Color,
    val exerciseWordPractice: Color,
    val exerciseAudition: Color,
    val exerciseGame: Color,
    val onImageCropperBackground: Color,
    val imageCropperBackground: Color,
    val imageCropperTransparentBackground: Color,
) {
    val primary = materialColors.primary
    val onPrimary = materialColors.onPrimary
    val primaryContainer = materialColors.primaryContainer
    val onPrimaryContainer = materialColors.onPrimaryContainer
    val secondary = materialColors.secondary
    val onSecondary = materialColors.onSecondary
    val secondaryContainer = materialColors.secondaryContainer
    val onSecondaryContainer = materialColors.onSecondaryContainer
    val background = materialColors.background
    val onBackground = materialColors.onBackground
    val surface = materialColors.surface
    val onSurface = materialColors.onSurface
    val surfaceVariant = materialColors.surfaceVariant
    val onSurfaceVariant = materialColors.onSurfaceVariant
    val error = materialColors.error
    val onError = materialColors.onError
}

val darkAppColors = AppColors(
    shimmerBackground = Color(0xFF111930),
    shimmerForeground = Color(0xFF1E2847),
    progressBackground = Color(0x4DFFFFFF),
    textBody = Color(0xFF8D8D8D),
    inputFieldBackground = Color(0x0DFFFFFF),
    inputFieldErrorBackground = Color(0x1AFF0000),
    inputFieldHint = Color(0x809699A3),
    inputFieldText = Color(0xFF363B44),
    success = Color(0xFF5BA890),
    onSuccess = Color(0xFFFFFFFF),
    exerciseGuessAnimal = Color(0xFF4A63C9),
    exerciseWordPractice = Color(0xFFAF144D),
    exerciseAudition = Color(0xFFC55000),
    exerciseGame = Color(0xFF50927E),
    imageCropperBackground = Color(0xFF1B1815),
    onImageCropperBackground = Color(0xFFFFFFFF),
    imageCropperTransparentBackground = Color(0x80000000),
    materialColors = darkColorScheme(
        primary = Color(0xFF410FA3),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFF76400),
        onPrimaryContainer = Color(0xFFFFFFFF),
        secondary = Color(0xFF5B7BFE),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFF5B7BFE),
        onSecondaryContainer = Color(0xFFFFFFFF),
        error = Color(0xFFD6185D),
        errorContainer = Color(0xFFD6185D),
        onError = Color(0xFFFFFFFF),
        onErrorContainer = Color(0xFFFFFFFF),
        background = Color(0xFF080E1E),
        onBackground = Color(0xFFFFFFFF),
        surface = Color(0xFF23252A),
        onSurface = Color(0xFFFFFFFF),
        surfaceVariant = Color(0xFFFFF6EB),
        onSurfaceVariant = Color(0xFF000000),
        scrim = Color(0x00000000),
    )
)

val lightAppColors = AppColors(
    shimmerBackground = Color(0xFFE0E0E0),
    shimmerForeground = Color(0xFFE7E7E7),
    progressBackground = Color(0x33080E1E),
    textBody = Color(0xFF8A8A8A),
    inputFieldBackground = Color(0x0D080E1E),
    inputFieldErrorBackground = Color(0x1AFF0000),
    inputFieldHint = Color(0x80656872),
    inputFieldText = Color(0xFF363B44),
    success = Color(0xFF5BA890),
    onSuccess = Color(0xFFFFFFFF),
    exerciseGuessAnimal = Color(0xFF5B7BFE),
    exerciseWordPractice = Color(0xFFD6185D),
    exerciseAudition = Color(0xFFF76400),
    exerciseGame = Color(0xFF5BA890),
    imageCropperBackground = Color(0xFF1B1815),
    onImageCropperBackground = Color(0xFFFFFFFF),
    imageCropperTransparentBackground = Color(0x80000000),
    materialColors = lightColorScheme(
        primary = Color(0xFF410FA3),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFF76400),
        onPrimaryContainer = Color(0xFF000000),
        secondary = Color(0xFF5B7BFE),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFF5B7BFE),
        onSecondaryContainer = Color(0xFFFFFFFF),
        error = Color(0xFFD6185D),
        errorContainer = Color(0xFFD6185D),
        onError = Color(0xFFFFFFFF),
        onErrorContainer = Color(0xFFFFFFFF),
        background = Color(0xFFFFFFFF),
        onBackground = Color(0xFF080E1E),
        surface = Color(0xFFE2E2E2),
        onSurface = Color(0xFF000000),
        surfaceVariant = Color(0xFFFFF6EB),
        onSurfaceVariant = Color(0xFF000000),
        scrim = Color(0x00000000),
    )
)

val LocalAppColors = staticCompositionLocalOf { lightAppColors }
