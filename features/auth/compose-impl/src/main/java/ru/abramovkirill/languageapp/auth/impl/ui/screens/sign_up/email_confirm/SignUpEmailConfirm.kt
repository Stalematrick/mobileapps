package ru.abramovkirill.languageapp.auth.impl.ui.screens.sign_up.email_confirm

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.abramovkirill.languageapp.auth.impl.ui.screens.sign_up.SignUpButtonsControllerEffect
import ru.abramovkirill.languageapp.auth.impl.ui.screens.sign_up.SignUpScreenContract
import ru.abramovkirill.languageapp.auth.impl.ui.screens.sign_up.SignUpViewModel
import ru.abramovkirill.languageapp.core.design.R
import ru.abramovkirill.languageapp.core.design.composables.AppRootContainer
import ru.abramovkirill.languageapp.core.design.styles.AppTheme
import ru.abramovkirill.languageapp.core.design.utils.COMPOSE_LARGE_DEVICE_SPEC

@Composable
fun SignUpEmailConfirmScreen(viewModel: SignUpViewModel) {
    SignUpEmailConfirmScreen(
        onIntent = viewModel::processIntent
    )
}

@Composable
internal fun SignUpEmailConfirmScreen(
    onIntent: (SignUpScreenContract.Intent) -> Unit,
) {
    SignUpButtonsControllerEffect(
        text = stringResource(R.string.finish_positive),
        isLoginButtonVisible = false,
        onClick = { onIntent(SignUpScreenContract.Intent.OnFinishButtonClick) }
    )

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 24.dp)
    ) {

        Image(
            imageVector = ImageVector.vectorResource(R.drawable.ic_round_check_24),
            contentDescription = null,
            colorFilter = ColorFilter.tint(AppTheme.colors.success),
            modifier = Modifier
                .size(64.dp),
        )

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = stringResource(ru.abramovkirill.languageapp.auth.impl.R.string.signup_waiting_for_email_confirm),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            style = AppTheme.typography.titleLarge,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(ru.abramovkirill.languageapp.auth.impl.R.string.signup_email_confirm_description),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            color = AppTheme.colors.textBody,
            style = AppTheme.typography.titleMedium,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
private fun SignUpEmailConfirmScreenPreview() {
    AppRootContainer { _, _ ->
        SignUpEmailConfirmScreen(
            onIntent = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun SignUpEmailConfirmScreenPreviewLight() {
    SignUpEmailConfirmScreenPreview()
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun SignUpEmailConfirmScreenPreviewDark() {
    SignUpEmailConfirmScreenPreview()
}

@Composable
@Preview(
    showBackground = true,
    device = COMPOSE_LARGE_DEVICE_SPEC,
)
private fun SignUpEmailConfirmScreenPreviewLightLarge() {
    SignUpEmailConfirmScreenPreview()
}

@Composable
@Preview(
    showBackground = true,
    device = COMPOSE_LARGE_DEVICE_SPEC,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
private fun SignUpEmailConfirmScreenPreviewDarkLarge() {
    SignUpEmailConfirmScreenPreview()
}
