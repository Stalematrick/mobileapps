package ru.abramovkirill.languageapp.profile.impl.domain

import ru.abramovkirill.languageapp.core.profiles.api.domain.ProfilesRepository

class CroppedAvatarUploadUseCase(
    private val profilesRepository: ProfilesRepository,
    private val avatarCropper: AvatarCropper,
) {
    suspend operator fun invoke(avatarUri: String, avatarCrop: AvatarCrop) {
        val avatarCroppingResult = avatarCropper.crop(
            avatarUri = avatarUri,
            avatarCrop = avatarCrop,
        )

        profilesRepository.updateCurrentProfileAvatar(
            avatarBytes = avatarCroppingResult.bytes,
            extension = avatarCroppingResult.extension,
        )
    }
}
