package ru.abramovkirill.languageapp.profile.impl.ui.screens.profile

import android.net.Uri
import ru.abramovkirill.languageapp.common.utils.PainterResource
import ru.abramovkirill.languageapp.common.utils.StringResource

object ProfileContract {

    data class State(
        val profileState: Profile = Profile.Loading,
        val isSignOutInProcess: Boolean = false,
        val isAvatarSourceBottomSheetShown: Boolean = false,
    ) {
        sealed interface Profile {
            data object Loading : Profile

            data class Loaded(
                val fullName: StringResource?,
                val avatar: PainterResource?
            ) : Profile
        }
    }

    sealed interface Intent {
        data class OnSwitchUiModeButtonClick(val toDarkTheme: Boolean) : Intent
        data class OnAvatarSourceButtonClick(val button: AvatarSourceButton) : Intent
        data class OnAvatarUriReceived(val uri: Uri) : Intent
        data object OnChangeLanguageButtonClick : Intent
        data object OnChangeAvatarButtonClick : Intent
        data object OnAvatarSourceRequestDismiss : Intent
        data object OnLogoutButtonClick : Intent
        data object OnGoBackClick : Intent
    }

    sealed interface SideEffect {
        data class Message(val text: StringResource) : SideEffect
        data object PickPhotoFromGallery : SideEffect
        data object TakePhoto : SideEffect
    }
}
