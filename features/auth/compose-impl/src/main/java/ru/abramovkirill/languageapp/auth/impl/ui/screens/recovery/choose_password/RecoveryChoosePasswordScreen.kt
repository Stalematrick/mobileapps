package ru.abramovkirill.languageapp.auth.impl.ui.screens.recovery.choose_password

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.abramovkirill.languageapp.auth.impl.R
import ru.abramovkirill.languageapp.auth.impl.ui.composables.choose_password.ChoosePassword
import ru.abramovkirill.languageapp.auth.impl.ui.composables.choose_password.ChoosePasswordContract
import ru.abramovkirill.languageapp.auth.impl.ui.screens.recovery.RecoveryButtonsControllerEffect
import ru.abramovkirill.languageapp.auth.impl.ui.screens.recovery.RecoveryFlowContract.Intent
import ru.abramovkirill.languageapp.auth.impl.ui.screens.recovery.RecoveryFlowContract.State
import ru.abramovkirill.languageapp.auth.impl.ui.screens.recovery.RecoveryFlowViewModel
import ru.abramovkirill.languageapp.common.utils.states.ProcessingState
import ru.abramovkirill.languageapp.core.design.composables.AppRootContainer
import ru.abramovkirill.languageapp.core.design.utils.COMPOSE_LARGE_DEVICE_SPEC
import ru.abramovkirill.languageapp.core.design.utils.smallDeviceMaxWidth

@Composable
fun RecoveryChoosePasswordScreen(viewModel: RecoveryFlowViewModel) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    RecoveryChoosePasswordScreen(
        state = state,
        onIntent = viewModel::processIntent
    )
}

@Composable
internal fun RecoveryChoosePasswordScreen(
    state: State,
    onIntent: (Intent) -> Unit,
) {
    RecoveryButtonsControllerEffect(
        text = stringResource(R.string.confirm_password),
        onClick = { onIntent(Intent.OnNewPasswordConfirmButtonClick) }
    )

    Column(
        modifier = Modifier
            .smallDeviceMaxWidth()
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 24.dp)
    ) {
        ChoosePassword(
            state = ChoosePasswordContract.State(
                password = state.password,
                confirmedPassword = state.confirmedPassword,
                passwordErrorMessage = state.passwordErrorMessage,
                confirmedPasswordErrorMessage = state.confirmedPasswordErrorMessage,
                isPasswordVisible = state.isPasswordVisible,
                isConfirmedPasswordVisible = state.isConfirmedPasswordVisible,
                isEnabled = state.recoveringState == ProcessingState.None,
            ),
            onIntent = { intent ->
                when (intent) {
                    is ChoosePasswordContract.Intent.OnPasswordChanged ->
                        onIntent(Intent.OnPasswordChanged(intent.text))
                    is ChoosePasswordContract.Intent.OnConfirmedPasswordChanged ->
                        onIntent(Intent.OnConfirmedPasswordChanged(intent.text))
                    ChoosePasswordContract.Intent.OnPasswordVisibilityToggleClick ->
                        onIntent(Intent.OnPasswordVisibilityToggleClick)
                    ChoosePasswordContract.Intent.OnConfirmedPasswordVisibilityToggleClick ->
                        onIntent(Intent.OnConfirmedPasswordVisibilityToggleClick)
                    ChoosePasswordContract.Intent.OnKeyboardDoneAction ->
                        onIntent(Intent.OnNewPasswordConfirmButtonClick)
                }
            }
        )
    }
}

@Composable
private fun RecoveryChoosePasswordScreenPreview() {
    AppRootContainer { _, _ ->
        RecoveryChoosePasswordScreen(
            state = State(),
            onIntent = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun RecoveryChoosePasswordScreenPreviewLight() {
    RecoveryChoosePasswordScreenPreview()
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun RecoveryChoosePasswordScreenPreviewDark() {
    RecoveryChoosePasswordScreenPreview()
}

@Composable
@Preview(
    showBackground = true,
    device = COMPOSE_LARGE_DEVICE_SPEC,
)
private fun RecoveryChoosePasswordScreenPreviewLightLarge() {
    RecoveryChoosePasswordScreenPreview()
}

@Composable
@Preview(
    showBackground = true,
    device = COMPOSE_LARGE_DEVICE_SPEC,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
private fun RecoveryChoosePasswordScreenPreviewDarkLarge() {
    RecoveryChoosePasswordScreenPreview()
}
