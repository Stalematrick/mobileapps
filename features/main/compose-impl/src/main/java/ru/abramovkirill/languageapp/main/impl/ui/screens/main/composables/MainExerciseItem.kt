package ru.abramovkirill.languageapp.main.impl.ui.screens.main.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.abramovkirill.languageapp.core.design.styles.AppTheme
import ru.abramovkirill.languageapp.main.impl.R

@Composable
fun MainExerciseItem(
    onClick: () -> Unit,
    image: Painter,
    title: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    includeSpacer: Boolean = false,
    isEnabled: Boolean = true,
) {
    Card(
        onClick = onClick,
        enabled = isEnabled,
        shape = AppTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
        ),
        modifier = modifier
            .padding(bottom = if (includeSpacer) 17.dp else 0.dp)
            .alpha(if (isEnabled) ENABLED_ALPHA else DISABLED_ALPHA)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Image(
            painter = image,
            contentDescription = title,
            modifier = Modifier
                .size(ExerciseImageSize)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = title,
            style = AppTheme.typography.bodyMedium,
            fontWeight = FontWeight.Normal,
            color = AppTheme.colors.onPrimary,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(10.dp))
    }
}

private val ExerciseImageSize = 90.dp
private const val ENABLED_ALPHA = 1f
private const val DISABLED_ALPHA = 0.4f

@Composable
@Preview(showBackground = true)
private fun MainExerciseItemPreview() {
    AppTheme {
        MainExerciseItem(
            onClick = {},
            image = painterResource(R.drawable.word),
            title = stringResource(R.string.main_word_practice),
            backgroundColor = AppTheme.colors.exerciseWordPractice,
            includeSpacer = false,
            modifier = Modifier
                .width(150.dp),
        )
    }
}
