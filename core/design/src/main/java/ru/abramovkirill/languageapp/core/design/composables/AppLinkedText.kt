package ru.abramovkirill.languageapp.core.design.composables

import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import ru.abramovkirill.languageapp.core.design.styles.AppTheme

@Composable
fun AppLinkedText(
    onClick: (AppLinkedTextUnit) -> Unit,
    units: List<AppLinkedTextUnit>,
    textSpanStyle: SpanStyle,
    linkSpanStyle: SpanStyle,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
) {
    val annotatedString = buildAnnotatedString {
        withStyle(textSpanStyle.withEnabled(isEnabled)) {
            units.forEachIndexed { index, unit ->
                if (unit.linkTag != null) {
                    withStyle(linkSpanStyle.withEnabled(isEnabled)) {
                        pushStringAnnotation(unit.linkTag, unit.linkTag)
                        append(unit.text)
                    }
                } else {
                    append(unit.text)
                }

                if (index != units.lastIndex) {
                    append(" ")
                }
            }
        }
    }

    ClickableText(
        text = annotatedString,
        onClick = { offset ->
            if (isEnabled) {
                val annotation = annotatedString
                    .getStringAnnotations(offset, offset)
                    .firstOrNull()

                val unit = units
                    .firstOrNull { it.linkTag == annotation?.tag }

                if (unit != null) {
                    onClick(unit)
                }
            }
        },
        modifier = modifier,
    )
}

data class AppLinkedTextUnit(
    val text: String,
    val linkTag: String? = null
)

private fun SpanStyle.withEnabled(isEnabled: Boolean) =
    copy(
        color = color.copy(
            alpha = if (isEnabled) ENABLED_ALPHA else DISABLED_ALPHA
        )
    )

private const val ENABLED_ALPHA = 1f
private const val DISABLED_ALPHA = 0.3f

@Composable
@Preview(showBackground = true)
private fun AppLinkedTextPreview() {
    AppTheme {
        AppLinkedText(
            units = listOf(
                AppLinkedTextUnit(
                    text = "You can use",
                ),
                AppLinkedTextUnit(
                    text = "Google",
                    linkTag = "Google"
                ),
                AppLinkedTextUnit(
                    text = "for sign in",
                ),
            ),
            textSpanStyle = SpanStyle(
                fontWeight = FontWeight.Medium,
                color = AppTheme.colors.textBody,
            ),
            linkSpanStyle = SpanStyle(
                color = AppTheme.colors.secondary,
            ),
            onClick = {}
        )
    }
}
