package ru.abramovkirill.languageapp.auth.impl.ui.screens.recovery.check_email

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
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.abramovkirill.languageapp.auth.impl.R
import ru.abramovkirill.languageapp.core.design.R as DesignR
import ru.abramovkirill.languageapp.auth.impl.ui.screens.recovery.RecoveryButtonsControllerEffect
import ru.abramovkirill.languageapp.auth.impl.ui.screens.recovery.RecoveryFlowContract.Intent
import ru.abramovkirill.languageapp.auth.impl.ui.screens.recovery.RecoveryFlowContract.State
import ru.abramovkirill.languageapp.auth.impl.ui.screens.recovery.RecoveryFlowViewModel
import ru.abramovkirill.languageapp.core.design.composables.AppRootContainer
import ru.abramovkirill.languageapp.core.design.styles.AppTheme
import ru.abramovkirill.languageapp.core.design.utils.COMPOSE_LARGE_DEVICE_SPEC


@Composable
fun RecoveryCheckEmailScreen(viewModel: RecoveryFlowViewModel) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    RecoveryCheckEmailScreen(
        state = state,
        onIntent = viewModel::processIntent
    )
}

@Composable
internal fun RecoveryCheckEmailScreen(
    state: State,
    onIntent: (Intent) -> Unit
) {
    RecoveryButtonsControllerEffect(
        text = stringResource(DesignR.string.ok_positive),
        onClick = { onIntent(Intent.OnCheckEmailOkButtonClick) }
    )

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Image(
            imageVector = ImageVector.vectorResource(ru.abramovkirill.languageapp.core.design.R.drawable.ic_round_check_24),
            contentDescription = null,
            colorFilter = ColorFilter.tint(AppTheme.colors.success),
            modifier = Modifier
                .size(64.dp),
        )

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = stringResource(R.string.password_recovery_check_email),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            style = AppTheme.typography.titleLarge,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.password_recovery_check_email_description, state.email),
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
private fun RecoveryCheckEmailScreenPreview() {
    AppRootContainer { _, _ ->
        RecoveryCheckEmailScreen(
            state = State(
                email = "example@email.com"
            ),
            onIntent = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun RecoveryCheckEmailScreenPreviewLight() {
    RecoveryCheckEmailScreenPreview()
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun RecoveryCheckEmailScreenPreviewDark() {
    RecoveryCheckEmailScreenPreview()
}

@Composable
@Preview(
    showBackground = true,
    device = COMPOSE_LARGE_DEVICE_SPEC,
)
private fun RecoveryCheckEmailScreenPreviewLightLarge() {
    RecoveryCheckEmailScreenPreview()
}

@Composable
@Preview(
    showBackground = true,
    device = COMPOSE_LARGE_DEVICE_SPEC,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
private fun RecoveryCheckEmailScreenPreviewDarkLarge() {
    RecoveryCheckEmailScreenPreview()
}
