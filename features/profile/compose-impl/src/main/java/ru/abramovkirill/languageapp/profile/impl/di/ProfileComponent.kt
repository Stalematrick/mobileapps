package ru.abramovkirill.languageapp.profile.impl.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import ru.abramovkirill.languageapp.core.env.EnvironmentConfig
import ru.abramovkirill.languageapp.profile.impl.ui.screens.avatar_crop.AvatarCropViewModel
import ru.abramovkirill.languageapp.profile.impl.ui.screens.select_language.SelectLanguageViewModel

object ProfileComponent : KoinComponent {

    val environmentConfig: EnvironmentConfig by inject()

    val profileViewModelFactory: ViewModelProvider.Factory
        get() = get<ProfileViewModelFactory>().instance

    fun getSelectLanguageViewModelFactory(
        canGoBack: Boolean,
    ) =
        viewModelFactory {
            initializer {
                SelectLanguageViewModel(
                    router = get(),
                    settingsRepository = get(),
                    canGoBack = canGoBack,
                )
            }
        }

    fun getAvatarCropViewModelFactory(
        photoUri: String,
    ) =
        viewModelFactory {
            initializer {
                AvatarCropViewModel(
                    photoUri = photoUri,
                    router = get(),
                    croppedAvatarUpload = get(),
                )
            }
        }
}
