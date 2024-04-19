package ru.abramovkirill.languageapp.core.design.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.abramovkirill.languageapp.common.utils.states.ProcessingState

fun CoroutineScope.withReturnToNone(
    startWith: ProcessingState,
    processingDelay: Long = 1000L,
    onState: suspend (ProcessingState) -> Unit
) {
    launch {
        onState(startWith)
        delay(processingDelay)
        onState(ProcessingState.None)
    }
}

fun CoroutineScope.withReturnToNone(
    startWith: ProcessingState,
    processingDelay: Long = 1000L,
    onStartState: suspend (ProcessingState) -> Unit,
    onEndState: suspend  (ProcessingState) -> Unit,
) {
    launch {
        onStartState(startWith)
        delay(processingDelay)
        onEndState(ProcessingState.None)
    }
}
