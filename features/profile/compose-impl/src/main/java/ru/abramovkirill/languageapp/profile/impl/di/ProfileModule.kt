package ru.abramovkirill.languageapp.profile.impl.di

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.koin.core.module.Module
import org.koin.dsl.module
import ru.abramovkirill.languageapp.core.coroutines.dispatchers.di.DefaultCoroutineDispatcherQualifier
import ru.abramovkirill.languageapp.core.coroutines.dispatchers.di.IoCoroutineDispatcherQualifier
import ru.abramovkirill.languageapp.profile.api.domain.SettingsRepository
import ru.abramovkirill.languageapp.profile.impl.data.AvatarCropperImpl
import ru.abramovkirill.languageapp.profile.impl.data.SettingsRepositoryImpl
import ru.abramovkirill.languageapp.profile.impl.data.SettingsStorage
import ru.abramovkirill.languageapp.profile.impl.domain.AvatarCropper
import ru.abramovkirill.languageapp.profile.impl.domain.CroppedAvatarUploadUseCase
import ru.abramovkirill.languageapp.profile.impl.ui.screens.profile.ProfileViewModel

val profileModule = module {
    single<SettingsStorage> {
        SettingsStorage(
            applicationContext = get(),
            storageDispatcher = get(IoCoroutineDispatcherQualifier)
        )
    }

    factory<SettingsRepository> {
        SettingsRepositoryImpl(
            settingsStorage = get(),
        )
    }

    useProfileScreenBeans()
    useAvatarCropperBeans()
}

private fun Module.useProfileScreenBeans() {
    single<ProfileViewModelFactory> {
        ProfileViewModelFactory(
            viewModelFactory {
                initializer {
                    ProfileViewModel(
                        router = get(),
                        profilesRepository = get(),
                        settingsRepository = get(),
                        signOut = get(),
                    )
                }
            }
        )
    }
}

private fun Module.useAvatarCropperBeans() {
    factory<AvatarCropper> {
        AvatarCropperImpl(
            cropperDispatcher = get(DefaultCoroutineDispatcherQualifier),
            applicationContext = get(),
            imageLoader = get(),
        )
    }

    factory<CroppedAvatarUploadUseCase> {
        CroppedAvatarUploadUseCase(
            profilesRepository = get(),
            avatarCropper = get(),
        )
    }
}
