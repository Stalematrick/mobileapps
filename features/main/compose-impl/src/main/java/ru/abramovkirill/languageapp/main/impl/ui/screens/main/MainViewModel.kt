package ru.abramovkirill.languageapp.main.impl.ui.screens.main

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import ru.abramovkirill.languageapp.common.utils.launchSafe
import ru.abramovkirill.languageapp.common.utils.painterRes
import ru.abramovkirill.languageapp.common.utils.strRes
import ru.abramovkirill.languageapp.core.design.R
import ru.abramovkirill.languageapp.core.design.utils.formatFullName
import ru.abramovkirill.languageapp.core.navigation.api.Router
import ru.abramovkirill.languageapp.core.profiles.api.domain.Profile
import ru.abramovkirill.languageapp.core.profiles.api.domain.ProfilesRepository
import ru.abramovkirill.languageapp.exercises.audition.api.ui.navigation.AUDITION_SCREEN_ROUTE_PATH
import ru.abramovkirill.languageapp.exercises.guess_animal.api.ui.navigation.GUESS_ANIMAL_SCREEN_ROUTE_PATH
import ru.abramovkirill.languageapp.exercises.word_practice.api.ui.navigation.WORD_PRACTICE_SCREEN_ROUTE_PATH
import ru.abramovkirill.languageapp.main.impl.ui.screens.main.MainScreenContract.Intent
import ru.abramovkirill.languageapp.main.impl.ui.screens.main.MainScreenContract.SideEffect
import ru.abramovkirill.languageapp.main.impl.ui.screens.main.MainScreenContract.State
import ru.abramovkirill.languageapp.profile.api.domain.SettingsRepository
import ru.abramovkirill.languageapp.profile.api.ui.navigation.PROFILE_GRAPH_ROUTE_PATH
import ru.abramovkirill.languageapp.profile.api.ui.navigation.SELECT_LANGUAGE_SCREEN_ROUTE_PATH
import ru.abramovkirill.languageapp.profile.api.ui.navigation.SelectLanguageScreenArguments

private typealias IntentBody = SimpleSyntax<State, SideEffect>

class MainViewModel(
    private val profilesRepository: ProfilesRepository,
    private val settingsRepository: SettingsRepository,
    private val router: Router,
) : ViewModel(), ContainerHost<State, SideEffect> {
    override val container = container<State, SideEffect>(
        initialState = State()
    )

    private var observeCurrentProfileJob: Job? = null
    private var observeLeaderboardJob: Job? = null

    init {
        loadData()
    }

    fun processIntent(intent: Intent) = intent {
        when (intent) {
            Intent.OnProfileClick ->
                onProfileClick()
            Intent.OnAuditionButtonClick ->
                onAuditionButtonClick()
            Intent.OnGameButtonClick ->
                onGameButtonClick()
            Intent.OnGuessAnimalButtonClick ->
                onGuessAnimalButtonClick()
            Intent.OnWordPracticeButtonClick ->
                onWordPracticeButtonClick()
            Intent.OnPulledToRefresh ->
                onPulledToRefresh()
        }
    }

    private fun loadData() {
        checkSavedLanguage()
        observeUserData(reload = false)
        observeLeaderboard()
    }

    private fun checkSavedLanguage() = intent {
        viewModelScope.launchSafe(
            block = {
                if (!settingsRepository.isLanguageAlreadySelected()) {
                    router.navigate(
                        routePath = SELECT_LANGUAGE_SCREEN_ROUTE_PATH,
                        arguments = mapOf(SelectLanguageScreenArguments.CAN_GO_BACK to false)
                    )
                }
            },
            onError = { /* ignored */ }
        ).join()
    }

    private fun observeUserData(reload: Boolean) = intent {
        viewModelScope.launchSafe(
            block = {
                reduce { state.copy(profileState = State.Profile.Loading) }

                observeCurrentProfileJob?.cancel()
                observeCurrentProfileJob = null

                observeCurrentProfileJob = profilesRepository.observeCurrentProfile(
                    reload = reload
                )
                    .onEach { profile ->
                        reduce {
                            state.copy(
                                profileState = State.Profile.Loaded(
                                    firstName = profile.firstName,
                                    avatar = profile.avatarUrl?.let { url ->
                                        painterRes(Uri.parse(url))
                                    },
                                )
                            )
                        }
                    }
                    .launchIn(this)
            },
            onError = {
                postSideEffect(SideEffect.Message(strRes(R.string.error_smth_went_wrong)))

                reduce {
                    state.copy(
                        profileState = State.Profile.Loaded(
                            firstName = null,
                            avatar = null,
                        )
                    )
                }
            }
        )
    }

    private fun observeLeaderboard() = intent {
        viewModelScope.launchSafe(
            block = {
                reduce { state.copy(leaderboard = State.Leaderboard.Loading) }

                observeLeaderboardJob?.cancel()
                observeLeaderboardJob = null
                val leaderboard       = listOf(
                    Profile(
                        id="1",
                        email="mailexample@com",
                        avatarUrl="",
                        firstName = "firstname",
                        lastName = "lastname",
                        totalScore = 10.0f
                        )
                )

                reduce {
                    state.copy(
                        leaderboard = State.Leaderboard.Loaded(
                            items = leaderboard.map(::toLeaderboardItem)
                                .toImmutableList()
                        )
                    )
                }
            },
            onError = {
                postSideEffect(SideEffect.Message(strRes(R.string.error_smth_went_wrong)))

                reduce {
                    state.copy(
                        leaderboard = State.Leaderboard.Loaded(
                            items = persistentListOf()
                        )
                    )
                }
            }
        )
    }

    private suspend fun onProfileClick() {
        router.navigate(routePath = PROFILE_GRAPH_ROUTE_PATH)
    }

    private suspend fun onGuessAnimalButtonClick() {
        router.navigate(routePath = GUESS_ANIMAL_SCREEN_ROUTE_PATH)
    }

    private suspend fun onWordPracticeButtonClick() {
        router.navigate(routePath = WORD_PRACTICE_SCREEN_ROUTE_PATH)
    }

    private suspend fun onAuditionButtonClick() {
        router.navigate(routePath = AUDITION_SCREEN_ROUTE_PATH)
    }

    private fun onGameButtonClick() {
        // TODO
    }

    private suspend fun IntentBody.onPulledToRefresh() {
        joinAll(observeUserData(reload = true), observeLeaderboard())

        viewModelScope.launch {
            reduce { state.copy(isRefreshing = true) }
            delay(PULL_TO_REFRESH_DURATION)
            reduce { state.copy(isRefreshing = false) }
        }
    }

    private fun toLeaderboardItem(profile: Profile) = State.Leaderboard.Item(
        id = profile.id,
        avatar = profile.avatarUrl?.let { url -> painterRes(Uri.parse(url)) },
        fullName = formatFullName(
            firstName = profile.firstName,
            lastName = profile.lastName
        ),
        totalScore = profile.totalScore,
    )

    companion object {
        private const val PULL_TO_REFRESH_DURATION = 1000L
    }
}
