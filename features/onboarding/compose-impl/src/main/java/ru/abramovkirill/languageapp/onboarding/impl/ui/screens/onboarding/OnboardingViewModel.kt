package ru.abramovkirill.languageapp.onboarding.impl.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import ru.abramovkirill.languageapp.auth.api.ui.navigation.AUTH_GRAPH_ROUTE_PATH
import ru.abramovkirill.languageapp.common.utils.strRes
import ru.abramovkirill.languageapp.common.utils.vectorRes
import ru.abramovkirill.languageapp.core.navigation.api.Router
import ru.abramovkirill.languageapp.core.navigation.api.RoutingOption
import ru.abramovkirill.languageapp.onboarding.api.domain.OnboardingRepository
import ru.abramovkirill.languageapp.onboarding.api.domain.models.OnboardingUnit
import ru.abramovkirill.languageapp.onboarding.api.ui.navigation.ONBOARDING_SCREEN_ROUTE_PATH
import ru.abramovkirill.languageapp.onboarding.impl.R
import ru.abramovkirill.languageapp.onboarding.impl.ui.screens.onboarding.OnboardingContract.Intent
import ru.abramovkirill.languageapp.onboarding.impl.ui.screens.onboarding.OnboardingContract.State

class OnboardingViewModel(
    private val onboardingRepository: OnboardingRepository,
    private val router: Router,
) : ViewModel(), ContainerHost<State, Unit> {
    override val container = container<State, Unit>(
        initialState = State.Loading
    )

    private val allUnitsFlow = MutableStateFlow(emptyList<OnboardingUnit>())
    private val progressFlow = MutableStateFlow(0)

    init {
        intent {
            allUnitsFlow.value = onboardingRepository.getUnwatchedUnits()

            allUnitsFlow
                .combine(progressFlow) { allUnits, progress ->
                    onboardingRepository.saveWatchedUnit(allUnits[progress])
                    reduceUnitState(allUnits, progress)
                }
                .launchIn(viewModelScope)
        }
    }

    fun processIntent(intent: Intent) = intent {
        when (intent) {
            Intent.Next -> {
                if (progressFlow.value < allUnitsFlow.value.size - 1) {
                    progressFlow.value += 1
                } else {
                    navigateToLoginScreen()
                }
            }
            Intent.Skip -> {
                onboardingRepository.saveWatchedUnits(allUnitsFlow.value)
                navigateToLoginScreen()
            }
        }
    }

    private suspend fun navigateToLoginScreen() {
        router.navigate(
            routePath = AUTH_GRAPH_ROUTE_PATH,
            options = listOf(
                RoutingOption.PopUpTo(
                    routePath = ONBOARDING_SCREEN_ROUTE_PATH,
                    inclusive = true,
                )
            )
        )
    }

    private suspend fun SimpleSyntax<State, Unit>.reduceUnitState(
        allUnits: List<OnboardingUnit>,
        progress: Int,
    ) {
        reduce {
            if (allUnits.isEmpty()) {
                State.Loading
            } else {
                mapUnitToState(
                    unit = allUnits[progress],
                    progress = progress,
                    total = allUnits.size
                )
            }
        }
    }

    private fun mapUnitToState(
        unit: OnboardingUnit,
        total: Int,
        progress: Int,
    ) = when (unit) {
        OnboardingUnit.CONVERSATION_BASED_LEARNING -> {
            State.Loaded(
                image = vectorRes(R.drawable.onboarding_conversation_based_learning),
                title = strRes(R.string.onboarding_conversation_based_learning_title),
                description = strRes(R.string.onboarding_conversation_based_learning_description),
                progress = progress,
                total = total,
            )
        }
        OnboardingUnit.ANYTIME_LEARNING -> {
            State.Loaded(
                image = vectorRes(R.drawable.onboarding_anytime_learning),
                title = strRes(R.string.onboarding_anytime_learning_title),
                description = strRes(R.string.onboarding_anytime_learning_description),
                progress = progress,
                total = total,
            )
        }
        OnboardingUnit.VARIETY_LEARNING -> {
            State.Loaded(
                image = vectorRes(R.drawable.onboarding_variety_learning),
                title = strRes(R.string.onboarding_variety_learning_title),
                description = strRes(R.string.onboarding_variety_learning_description),
                progress = progress,
                total = total,
            )
        }
    }
}
