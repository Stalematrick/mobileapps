package ru.abramovkirill.languageapp.auth.impl.ui.screens.recovery.enter_email

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.abramovkirill.languageapp.auth.impl.R
import ru.abramovkirill.languageapp.auth.impl.ui.screens.recovery.RecoveryButtonsControllerEffect
import ru.abramovkirill.languageapp.auth.impl.ui.screens.recovery.RecoveryFlowContract.Intent
import ru.abramovkirill.languageapp.auth.impl.ui.screens.recovery.RecoveryFlowContract.State
import ru.abramovkirill.languageapp.auth.impl.ui.screens.recovery.RecoveryFlowViewModel
import ru.abramovkirill.languageapp.common.utils.extract
import ru.abramovkirill.languageapp.common.utils.states.ProcessingState
import ru.abramovkirill.languageapp.core.design.composables.AppRootContainer
import ru.abramovkirill.languageapp.core.design.composables.text_field.AppTextField
import ru.abramovkirill.languageapp.core.design.styles.AppTheme
import ru.abramovkirill.languageapp.core.design.utils.COMPOSE_LARGE_DEVICE_SPEC
import ru.abramovkirill.languageapp.core.design.utils.smallDeviceMaxWidth

@Composable
fun RecoveryEnterEmailScreen(viewModel: RecoveryFlowViewModel) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    RecoveryEnterEmailScreen(
        state = state,
        onIntent = viewModel::processIntent
    )
}

@Composable
internal fun RecoveryEnterEmailScreen(
    state: State,
    onIntent: (Intent) -> Unit
) {
    RecoveryButtonsControllerEffect(
        text = stringResource(R.string.password_recovery_reset_password),
        onClick = { onIntent(Intent.OnResetPasswordButtonClick) }
    )

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .smallDeviceMaxWidth()
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = stringResource(R.string.enter_email),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            style = AppTheme.typography.titleLarge,
            modifier = Modifier
                .padding(horizontal = 56.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(32.dp))

        AppTextField(
            value = state.email,
            isEnabled = state.recoveringState == ProcessingState.None,
            label = stringResource(R.string.email_address),
            placeholder = stringResource(R.string.email),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Email,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onIntent(Intent.OnResetPasswordButtonClick)
                }
            ),
            errorMessage = state.emailErrorMessage?.extract(),
            onValueChange = { onIntent(Intent.OnEmailChanged(it)) },
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
private fun RecoveryEnterEmailPreview() {
    AppRootContainer { _, _ ->
        RecoveryEnterEmailScreen(
            state = State(),
            onIntent = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun RecoveryEnterEmailPreviewLight() {
    RecoveryEnterEmailPreview()
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun RecoveryEnterEmailPreviewDark() {
    RecoveryEnterEmailPreview()
}

@Composable
@Preview(
    showBackground = true,
    device = COMPOSE_LARGE_DEVICE_SPEC,
)
private fun RecoveryEnterEmailPreviewLightLarge() {
    RecoveryEnterEmailPreview()
}

@Composable
@Preview(
    showBackground = true,
    device = COMPOSE_LARGE_DEVICE_SPEC,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
private fun RecoveryEnterEmailPreviewDarkLarge() {
    RecoveryEnterEmailPreview()
}
