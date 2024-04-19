package ru.abramovkirill.languageapp.common.utils.compose

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.abramovkirill.languageapp.common.utils.StringResource
import ru.abramovkirill.languageapp.common.utils.extract

@Composable
fun <T> ScreenSideEffectsFlowHandler(
    sideEffects: Flow<T>,
    onSideEffect: suspend (T) -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(sideEffects, context) {
        sideEffects
            .onEach(onSideEffect)
            .launchIn(this)
    }
}

suspend fun handleMessageSideEffect(
    message: StringResource,
    snackbarHostState: SnackbarHostState,
    context: Context,
) {
    snackbarHostState.showSnackbar(message.extract(context) ?: return)
}
