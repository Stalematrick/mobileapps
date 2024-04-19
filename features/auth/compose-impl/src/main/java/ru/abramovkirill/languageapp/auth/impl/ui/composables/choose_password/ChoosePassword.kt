package ru.abramovkirill.languageapp.auth.impl.ui.composables.choose_password

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.abramovkirill.languageapp.auth.impl.R
import ru.abramovkirill.languageapp.auth.impl.ui.composables.choose_password.ChoosePasswordContract.Intent
import ru.abramovkirill.languageapp.auth.impl.ui.composables.choose_password.ChoosePasswordContract.State
import ru.abramovkirill.languageapp.common.utils.extract
import ru.abramovkirill.languageapp.core.design.composables.AppRootContainer
import ru.abramovkirill.languageapp.core.design.composables.text_field.AppPasswordTextField
import ru.abramovkirill.languageapp.core.design.styles.AppTheme

@Composable
fun ChoosePassword(
    state: State,
    onIntent: (Intent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.signup_choose_password),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            style = AppTheme.typography.titleLarge,
            modifier = Modifier
                .padding(horizontal = 56.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(32.dp))

        AppPasswordTextField(
            value = state.password,
            isPasswordVisible = state.isPasswordVisible,
            isEnabled = state.isEnabled,
            label = stringResource(R.string.password),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Password,
            ),
            errorMessage = state.passwordErrorMessage?.extract(),
            onValueChange = { onIntent(Intent.OnPasswordChanged(it)) },
            onPasswordVisibilityToggleClick = { onIntent(Intent.OnPasswordVisibilityToggleClick) },
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        AppPasswordTextField(
            value = state.confirmedPassword,
            isPasswordVisible = state.isConfirmedPasswordVisible,
            isEnabled = state.isEnabled,
            label = stringResource(R.string.confirm_password),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onIntent(Intent.OnKeyboardDoneAction)
                }
            ),
            errorMessage = state.confirmedPasswordErrorMessage?.extract(),
            onValueChange = { onIntent(Intent.OnConfirmedPasswordChanged(it)) },
            onPasswordVisibilityToggleClick = { onIntent(Intent.OnConfirmedPasswordVisibilityToggleClick) },
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
private fun ChoosePasswordPreview() {
    AppRootContainer { _, _ ->
        ChoosePassword(
            state = State(),
            onIntent = {}
        )
    }
}

@Composable
@Preview(
    showBackground = true,
)
private fun ChoosePasswordPreviewLight() {
    ChoosePasswordPreview()
}

@Composable
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
private fun ChoosePasswordPreviewDark() {
    ChoosePasswordPreview()
}
