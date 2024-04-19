package ru.abramovkirill.languageapp.presentation

import android.content.Intent
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.http.Url
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ru.abramovkirill.languageapp.auth.api.domain.AuthRepository
import ru.abramovkirill.languageapp.auth.api.ui.navigation.AUTH_GRAPH_ROUTE_PATH
import ru.abramovkirill.languageapp.common.utils.launchSafe
import ru.abramovkirill.languageapp.core.navigation.api.DeepLinkRouter
import ru.abramovkirill.languageapp.core.navigation.api.Router
import ru.abramovkirill.languageapp.core.navigation.api.RoutingOption
import ru.abramovkirill.languageapp.features.splash.api.navigation.SPLASH_SCREEN_ROUTE_PATH
import ru.abramovkirill.languageapp.main.api.ui.navigation.MAIN_SCREEN_ROUTE_PATH
import ru.abramovkirill.languageapp.onboarding.api.domain.OnboardingRepository
import ru.abramovkirill.languageapp.onboarding.api.ui.navigation.ONBOARDING_SCREEN_ROUTE_PATH

class InitializationViewModel(
    private val onboardingRepository: OnboardingRepository,
    private val authRepository: AuthRepository,
    private val router: Router,
    private val deepLinkRouters: List<DeepLinkRouter>,
    private val savedStateHandle: SavedStateHandle,
    intent: Intent,
) : ViewModel() {
    private val _isInitializationFinished = MutableStateFlow(
        savedStateHandle.get<Boolean>(HANDLE_INITIALIZATION_FINISHED_KEY) == true
    )

    val isInitializationFinished = _isInitializationFinished.asStateFlow()

    init {
        init(intent)

        isInitializationFinished
            .onEach { isFinished ->
                savedStateHandle[HANDLE_INITIALIZATION_FINISHED_KEY] = isFinished
            }
            .launchIn(viewModelScope)
    }

    fun onNewIntent(intent: Intent?) {
        if (intent != null) {
            viewModelScope.launch {
                handleDeepLinks(intent)
            }
        }
    }

    private fun init(intent: Intent) {
        if (isInitializationFinished.value) {
            return
        }

        viewModelScope.launchSafe(
            block = {
                val delayJob = launch {
                    delay(INITIALIZATION_DELAY_MILLIS)
                }

                val authInitializationJob = launch {
                    authRepository.awaitInitialization()
                }

                val splashInitializationJob = async {
                    when {
                        onboardingRepository.getUnwatchedUnits().isNotEmpty() ->
                            ONBOARDING_SCREEN_ROUTE_PATH
                        authRepository.hasSavedSession() ->
                            MAIN_SCREEN_ROUTE_PATH
                        else ->
                            AUTH_GRAPH_ROUTE_PATH
                    }
                }

                joinAll(
                    delayJob,
                    authInitializationJob,
                    splashInitializationJob
                )

                val nextScreenRoutePath = splashInitializationJob.await()

                onInitializationJobComplete(
                    nextScreenRoutePath = nextScreenRoutePath,
                    intent = intent
                )
            },
            onError = {
                onInitializationJobComplete(
                    nextScreenRoutePath = AUTH_GRAPH_ROUTE_PATH,
                    intent = intent
                )
            }
        )
    }

    private suspend fun onInitializationJobComplete(
        nextScreenRoutePath: String,
        intent: Intent
    ) {
        router.navigate(
            routePath = nextScreenRoutePath,
            options = listOf(
                RoutingOption.PopUpTo(
                    routePath = SPLASH_SCREEN_ROUTE_PATH,
                    inclusive = true
                )
            )
        )

        handleDeepLinks(intent)

        _isInitializationFinished.value = true
    }

    private suspend fun handleDeepLinks(intent: Intent) {
        intent.data?.toString()?.let { uriString ->
            deepLinkRouters.any { router ->
                router.handle(Url(uriString))
            }
        }
    }

    companion object {
        private const val INITIALIZATION_DELAY_MILLIS = 1000L
        private const val HANDLE_INITIALIZATION_FINISHED_KEY = "initialization_finished"
    }
}
