package ru.abramovkirill.languageapp.auth.impl.ui.navigation

import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.http.Url
import ru.abramovkirill.languageapp.auth.api.domain.AuthRepository
import ru.abramovkirill.languageapp.auth.impl.data.AuthRepositoryImpl
import ru.abramovkirill.languageapp.auth.impl.ui.navigation.recovery.RECOVERY_CHOOSE_PASSWORD_SCREEN_ROUTE_PATH
import ru.abramovkirill.languageapp.auth.impl.ui.navigation.recovery.RECOVERY_ENTER_EMAIL_SCREEN_ROUTE_PATH
import ru.abramovkirill.languageapp.auth.impl.ui.navigation.recovery.RECOVERY_ERROR_SCREEN_ROUTE_PATH
import ru.abramovkirill.languageapp.auth.impl.ui.navigation.recovery.RECOVERY_FLOW_ROUTE_PATH
import ru.abramovkirill.languageapp.auth.impl.ui.navigation.sign_in.SIGN_IN_SCREEN_ROUTE_PATH
import ru.abramovkirill.languageapp.core.navigation.api.DeepLinkRouter
import ru.abramovkirill.languageapp.core.navigation.api.Router
import ru.abramovkirill.languageapp.core.navigation.api.RoutingOption

internal class AuthDeepLinkRouter(
    private val router: Router,
    private val recoveryRouter: Router,
    private val authRepository: AuthRepository,
) : DeepLinkRouter {
    override suspend fun handle(data: Url): Boolean {
        when {
            isEmailConfirmDeepLink(data) -> {
                navigateToSignIn()
                return true
            }
            isRecoveryConfirmDeepLink(data) -> {
                val params = parseParams(data.encodedQuery)

                try {
                    val code = params[CODE_QUERY_KEY]
                        ?: throw IllegalArgumentException("No code in deeplink")
                    authRepository.applyRecoveryCode(code)
                } catch (e: IllegalArgumentException) {
                    navigateToErrorScreen()
                } catch (e: HttpRequestException) {
                    navigateToErrorScreen()
                } catch (e: HttpRequestTimeoutException) {
                    navigateToErrorScreen()
                } catch (e: RestException) {
                    navigateToErrorScreen()
                }

                if (recoveryRouter.currentRoute == null) {
                    navigateToRecoveryFlow()
                    navigateToChoosePasswordScreen(skipOtherScreens = true)
                } else {
                    navigateToChoosePasswordScreen()
                }

                return true
            }
        }

        return false
    }

    private suspend fun navigateToSignIn() {
        router.navigate(
            routePath = SIGN_IN_SCREEN_ROUTE_PATH,
            options = listOf(
                RoutingOption.PopUpTo(
                    routePath = SIGN_IN_SCREEN_ROUTE_PATH,
                    inclusive = false,
                ),
                RoutingOption.LaunchSingleTop(true),
            )
        )
    }

    private suspend fun navigateToRecoveryFlow() {
        router.navigate(routePath = RECOVERY_FLOW_ROUTE_PATH)
    }

    private suspend fun navigateToChoosePasswordScreen(skipOtherScreens: Boolean = false) {
        recoveryRouter.navigate(
            routePath = RECOVERY_CHOOSE_PASSWORD_SCREEN_ROUTE_PATH,
            options = listOf(
                RoutingOption.PopUpTo(
                    routePath = RECOVERY_ENTER_EMAIL_SCREEN_ROUTE_PATH,
                    inclusive = skipOtherScreens,
                ),
                RoutingOption.LaunchSingleTop(true),
            )
        )
    }

    private suspend fun navigateToErrorScreen() {
        recoveryRouter.navigate(
            routePath = RECOVERY_ERROR_SCREEN_ROUTE_PATH,
            options = listOf(
                RoutingOption.PopUpTo(
                    routePath = RECOVERY_ENTER_EMAIL_SCREEN_ROUTE_PATH,
                    inclusive = true,
                )
            )
        )
    }

    private fun parseParams(query: String?): Map<String, String> {
        val queries = query?.split('&') ?: return emptyMap()
        val params = mutableMapOf<String, String>()

        queries.forEach { queryPairStr ->
            val keyValue = queryPairStr.split('=')
            if (keyValue.size == 2) {
                params[keyValue.first()] = keyValue.last()
            }
        }

        return params
    }

    private fun isEmailConfirmDeepLink(data: Url) =
        data.pathSegments.contains(AuthRepositoryImpl.EMAIL_CONFIRM_URL_PATH)

    private fun isRecoveryConfirmDeepLink(data: Url) =
        data.pathSegments.contains(AuthRepositoryImpl.RECOVERY_CONFIRM_URL_PATH)

    companion object {
        private const val CODE_QUERY_KEY = "code"
    }
}
