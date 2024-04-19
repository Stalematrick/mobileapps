package ru.abramovkirill.languageapp.features.splash.impl.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.abramovkirill.languageapp.core.design.R
import ru.abramovkirill.languageapp.core.design.composables.AppRootContainer
import ru.abramovkirill.languageapp.core.design.composables.scaffold.AppScaffoldControllerEffect
import ru.abramovkirill.languageapp.core.design.styles.AppTheme
import ru.abramovkirill.languageapp.core.design.styles.Sizes

@Composable
fun SplashScreen() {
    AppScaffoldControllerEffect()

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(color = AppTheme.colors.primary)
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.ic_logo_28),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .size(
                    width = Sizes.SplashLogoWidth,
                    height = Sizes.SplashLogoHeight
                )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.app_name),
            color = AppTheme.colors.onPrimary,
            style = AppTheme.typography.displaySmall,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
@Preview
private fun SplashScreenPreview() {
    AppRootContainer { _, _ ->
        SplashScreen()
    }
}
