package ru.abramovkirill.languageapp.profile.impl.ui.screens.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import ru.abramovkirill.languageapp.auth.api.domain.AuthSignOutUseCase
import ru.abramovkirill.languageapp.common.utils.launchSafe
import ru.abramovkirill.languageapp.common.utils.painterRes
import ru.abramovkirill.languageapp.common.utils.strRes
import ru.abramovkirill.languageapp.core.design.utils.formatFullName
import ru.abramovkirill.languageapp.core.navigation.api.Router
import ru.abramovkirill.languageapp.core.profiles.api.domain.ProfilesRepository
import ru.abramovkirill.languageapp.profile.api.domain.ForcedTheme
import ru.abramovkirill.languageapp.profile.api.domain.SettingsRepository
import ru.abramovkirill.languageapp.profile.api.ui.navigation.SELECT_LANGUAGE_SCREEN_ROUTE_PATH
import ru.abramovkirill.languageapp.profile.api.ui.navigation.SelectLanguageScreenArguments
import ru.abramovkirill.languageapp.profile.impl.ui.navigation.AVATAR_CROP_SCREEN_ROUTE_PATH
import ru.abramovkirill.languageapp.profile.impl.ui.navigation.AvatarCropScreenArguments
import ru.abramovkirill.languageapp.profile.impl.ui.screens.profile.ProfileContract.Intent
import ru.abramovkirill.languageapp.profile.impl.ui.screens.profile.ProfileContract.SideEffect
import ru.abramovkirill.languageapp.profile.impl.ui.screens.profile.ProfileContract.State
import ru.abramovkirill.languageapp.core.design.R as DesignR

private typealias IntentBody = SimpleSyntax<State, SideEffect>

class ProfileViewModel(
    private val router: Router,
    private val profilesRepository: ProfilesRepository,
    private val settingsRepository: SettingsRepository,
    private val signOut: AuthSignOutUseCase,
) : ViewModel(), ContainerHost<State, SideEffect> {
    override val container = container<State, SideEffect>(
        initialState = State()
    )

    private var observeCurrentProfileJob: Job? = null

    init {
        loadData()
    }

    fun processIntent(intent: Intent) = intent {
        when (intent) {
            Intent.OnGoBackClick ->
                onGoBackClick()
            Intent.OnChangeAvatarButtonClick ->
                onChangeAvatarButtonClick()
            Intent.OnAvatarSourceRequestDismiss ->
                onAvatarSourceRequestDismiss()
            Intent.OnChangeLanguageButtonClick ->
                onChangeLanguageButtonClick()
            Intent.OnLogoutButtonClick ->
                onLogoutButtonClick()
            is Intent.OnSwitchUiModeButtonClick ->
                onSwitchUiModeButtonClick(intent.toDarkTheme)
            is Intent.OnAvatarSourceButtonClick ->
                onAvatarSourceButtonClick(intent.button)
            is Intent.OnAvatarUriReceived ->
                onAvatarUriReceived(intent.uri)
        }
    }

    private fun loadData() {
        observeProfileData()
    }

    private fun observeProfileData() = intent {
        viewModelScope.launchSafe(
            block = {
                reduce { state.copy(profileState = State.Profile.Loading) }

                observeCurrentProfileJob?.cancel()
                observeCurrentProfileJob = null

                observeCurrentProfileJob = profilesRepository.observeCurrentProfile()
                    .onEach { profile ->
                        reduce {
                            state.copy(
                                profileState = State.Profile.Loaded(
                                    fullName = formatFullName(
                                        firstName = profile.firstName,
                                        lastName = profile.lastName,
                                    ),
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
                postSideEffect(SideEffect.Message(strRes(DesignR.string.error_smth_went_wrong)))

                reduce {
                    state.copy(
                        profileState = State.Profile.Loaded(
                            fullName = null,
                            avatar = null,
                        )
                    )
                }
            }
        )
    }

    private suspend fun onGoBackClick() {
        router.navigateBack()
    }

    private suspend fun IntentBody.onChangeAvatarButtonClick() {
        reduce {
            state.copy(
                isAvatarSourceBottomSheetShown = true
            )
        }
    }

    private suspend fun IntentBody.onAvatarSourceRequestDismiss() {
        reduce {
            state.copy(
                isAvatarSourceBottomSheetShown = false
            )
        }
    }

    private suspend fun onChangeLanguageButtonClick() {
        router.navigate(
            routePath = SELECT_LANGUAGE_SCREEN_ROUTE_PATH,
            arguments = mapOf(
                SelectLanguageScreenArguments.CAN_GO_BACK to true
            )
        )
    }

    private suspend fun onLogoutButtonClick() = intent {
        viewModelScope.launchSafe(
            block = {
                reduce { state.copy(isSignOutInProcess = true) }

                signOut()

                reduce { state.copy(isSignOutInProcess = false) }
            },
            onError = {
                postSideEffect(SideEffect.Message(strRes(DesignR.string.error_smth_went_wrong)))
            }
        )
    }

    private suspend fun onSwitchUiModeButtonClick(toDarkTheme: Boolean) {
        settingsRepository.forceTheme(
            if (toDarkTheme) {
                ForcedTheme.DARK
            } else {
                ForcedTheme.LIGHT
            }
        )
    }

    private suspend fun IntentBody.onAvatarSourceButtonClick(button: AvatarSourceButton) {
        reduce { state.copy(isAvatarSourceBottomSheetShown = false) }

        when (button) {
            AvatarSourceButton.Gallery ->
                postSideEffect(SideEffect.PickPhotoFromGallery)
            AvatarSourceButton.Camera ->
                postSideEffect(SideEffect.TakePhoto)
        }
    }

    private suspend fun onAvatarUriReceived(uri: Uri) {
        router.navigate(
            routePath = AVATAR_CROP_SCREEN_ROUTE_PATH,
            arguments = mapOf(
                AvatarCropScreenArguments.PHOTO_URI to uri.toString()
            )
        )
    }
}
