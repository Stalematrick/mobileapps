package ru.abramovkirill.languageapp.core.design.utils

import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

const val SMALL_DEVICE_MAX_WIDTH_DP = 500

const val COMPOSE_LARGE_DEVICE_SPEC =
    "spec:width=700dp, " +
            "height=1000dp, " +
            "orientation=portrait"

@Composable
fun isLargeScreen(): Boolean {
    val configuration = LocalConfiguration.current
    return configuration.screenWidthDp > SMALL_DEVICE_MAX_WIDTH_DP
}

fun Modifier.smallDeviceMaxWidth() =
    then(
        Modifier
            .widthIn(max = SMALL_DEVICE_MAX_WIDTH_DP.dp)
    )
