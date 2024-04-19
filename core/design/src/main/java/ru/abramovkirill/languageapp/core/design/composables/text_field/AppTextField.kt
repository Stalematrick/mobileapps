package ru.abramovkirill.languageapp.core.design.composables.text_field

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.abramovkirill.languageapp.core.design.styles.AppTheme

@Composable
fun AppTextField(
    value: String,
    placeholder: String,
    label: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    errorMessage: String? = null,
) {
    var bufferedValue by remember { mutableStateOf(value) }

    Column(
        modifier = modifier,
    ) {
        Text(
            text = buildAnnotatedString {
                append(label)

                withStyle(
                    SpanStyle(
                        color = AppTheme.colors.error
                    )
                ) {
                    if (!errorMessage.isNullOrBlank()) {
                        append(" ")
                        append(errorMessage)
                    }
                }
            },
            style = AppTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = bufferedValue,
            onValueChange = { newValue ->
                bufferedValue = newValue
                onValueChange(newValue)
            },
            enabled = isEnabled,
            isError = !errorMessage.isNullOrBlank(),
            shape = AppTheme.shapes.medium,
            visualTransformation = visualTransformation,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            trailingIcon = trailingIcon,
            colors = textFieldColors(
                isError = !errorMessage.isNullOrBlank(),
                isEnabled = isEnabled
            ),
            placeholder = {
                Text(
                    text = placeholder,
                    color = AppTheme.colors.inputFieldHint
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        )
    }
}

@Composable
fun AppTextField(
    value: String,
    placeholder: String,
    label: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    errorMessage: String? = null,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = buildAnnotatedString {
                append(label)

                withStyle(
                    SpanStyle(
                        color = AppTheme.colors.error
                    )
                ) {
                    if (!errorMessage.isNullOrBlank()) {
                        append(" ")
                        append(errorMessage)
                    }
                }
            },
            style = AppTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = value,
            onValueChange = {},
            enabled = false,
            isError = !errorMessage.isNullOrBlank(),
            shape = AppTheme.shapes.medium,
            visualTransformation = visualTransformation,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            trailingIcon = trailingIcon,
            colors = textFieldColors(
                isError = !errorMessage.isNullOrBlank(),
                isEnabled = true
            ),
            placeholder = {
                Text(
                    text = placeholder,
                    color = AppTheme.colors.inputFieldHint
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        )
    }
}

@Composable
private fun textFieldColors(
    isError: Boolean,
    isEnabled: Boolean,
): TextFieldColors {
    val backgroundColor = when {
        isError -> {
            AppTheme.colors.inputFieldErrorBackground
        }
        else -> {
            AppTheme.colors.inputFieldBackground
        }
    }

    val textColor = when {
        !isEnabled -> {
            AppTheme.colors.onBackground
                .copy(alpha = DISABLED_ALPHA)
        }
        else -> {
            AppTheme.colors.onBackground
        }
    }
    
    return TextFieldDefaults.colors(
        focusedTextColor = textColor,
        unfocusedTextColor = textColor,
        disabledTextColor = textColor,
        errorTextColor = textColor,
        unfocusedContainerColor = backgroundColor,
        focusedContainerColor = backgroundColor,
        errorContainerColor = backgroundColor,
        disabledContainerColor = backgroundColor,
        unfocusedIndicatorColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent,
    )
}

private const val DISABLED_ALPHA = 0.2f

@Composable
@Preview(showBackground = true)
private fun AppTextFieldPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .wrapContentSize()
        ) {
            AppTextField(
                isEnabled = false,
                value = "",
                placeholder = "Надо что-то ввести",
                errorMessage = "Ошибка",
                label = "Здесь ты пишешь что-то",
                onValueChange = { /* ... */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(12.dp)
            )
        }
    }
}
