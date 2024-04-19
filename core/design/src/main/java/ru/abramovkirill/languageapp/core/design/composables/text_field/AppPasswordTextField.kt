package ru.abramovkirill.languageapp.core.design.composables.text_field

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import ru.abramovkirill.languageapp.core.design.R
import ru.abramovkirill.languageapp.core.design.styles.AppTheme

@Composable
fun AppPasswordTextField(
    value: String,
    label: String,
    isPasswordVisible: Boolean,
    onValueChange: (String) -> Unit,
    onPasswordVisibilityToggleClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    errorMessage: String? = null,
) {
    AppTextField(
        value = value,
        onValueChange = onValueChange,
        errorMessage = errorMessage,
        singleLine = true,
        isEnabled = isEnabled,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions.copy(
            keyboardType = KeyboardType.Password,
        ),
        visualTransformation = if (isPasswordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation(mask = PASSWORD_MASK)
        },
        trailingIcon = {
            IconButton(
                onClick = onPasswordVisibilityToggleClick
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_password_visibility),
                    tint = AppTheme.colors.inputFieldHint,
                    contentDescription = stringResource(R.string.toggle_password_visibility)
                )
            }
        },
        placeholder = PASSWORD_HINT,
        label = label,
        modifier = modifier,
    )
}

private const val PASSWORD_PLACEHOLDER_LENGTH = 7
private const val PASSWORD_MASK = '\u25CF'
private val PASSWORD_HINT = PASSWORD_MASK.toString().repeat(PASSWORD_PLACEHOLDER_LENGTH)

class PasswordVisibilityParameterProvider : PreviewParameterProvider<Boolean> {
    override val values = sequenceOf(false, true)
}

@Composable
@Preview(showBackground = true)
private fun AppPasswordTextFieldPreview(
    @PreviewParameter(PasswordVisibilityParameterProvider::class) isPasswordVisible: Boolean,
) {
    AppTheme {
        Box(
            modifier = Modifier
                .wrapContentSize()
        ) {
            AppPasswordTextField(
                value = "",
                isPasswordVisible = isPasswordVisible,
                label = "Здесь ты пишешь что-то",
                errorMessage = "Ошибка!",
                onValueChange = {},
                onPasswordVisibilityToggleClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(12.dp)
            )
        }
    }
}
