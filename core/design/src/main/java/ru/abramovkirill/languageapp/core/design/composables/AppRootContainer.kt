package ru.abramovkirill.languageapp.core.design.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ru.abramovkirill.languageapp.common.utils.consumeClicks
import ru.abramovkirill.languageapp.core.design.composables.scaffold.AppBarState
import ru.abramovkirill.languageapp.core.design.composables.scaffold.AppScaffold
import ru.abramovkirill.languageapp.core.design.composables.scaffold.AppScaffoldController
import ru.abramovkirill.languageapp.core.design.composables.scaffold.AppSystemBarsStyles
import ru.abramovkirill.languageapp.core.design.composables.scaffold.LocalAppScaffoldController
import ru.abramovkirill.languageapp.core.design.composables.scaffold.LocalAppSystemBarsStylesDefault
import ru.abramovkirill.languageapp.core.design.styles.AppTheme
import ru.abramovkirill.languageapp.core.design.styles.isAppInDarkTheme

@Composable
fun AppRootContainer(
    isDarkTheme: Boolean = isAppInDarkTheme(),
    content: @Composable BoxScope.(
        isDarkTheme: Boolean,
        systemBarsColors: AppSystemBarsStyles
    ) -> Unit,
) {
    AppTheme(isDarkTheme = isDarkTheme) {
        val appScaffoldController = remember(isDarkTheme) {
            AppScaffoldController(
                snackbarHostState = SnackbarHostState(),
            )
        }

        val currentAppBarState = appScaffoldController.appBarStates.lastOrNull()
            ?: AppBarState.Hidden

        val appSystemBarsStylesDefault = AppSystemBarsStyles.default(isDarkTheme = isDarkTheme)

        val appSystemBarsStyles = appScaffoldController.systemBarsStyles.lastOrNull()
            ?: appSystemBarsStylesDefault

        AppScaffold(
            snackbarHostState = appScaffoldController.snackbarHostState,
            appBarState = currentAppBarState,
            modifier = Modifier.fillMaxSize(),
        ) {
            CompositionLocalProvider(
                LocalAppScaffoldController provides appScaffoldController,
                LocalAppSystemBarsStylesDefault provides appSystemBarsStylesDefault,
            ) {
                content(isDarkTheme, appSystemBarsStyles)
            }
        }

        if (appScaffoldController.isNavigationTransitionRunning) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .consumeClicks()
            )
        }
    }
}
