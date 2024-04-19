package ru.abramovkirill.languageapp.core.profile.test

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.abramovkirill.languageapp.core.profiles.api.domain.Profile
import ru.abramovkirill.languageapp.core.profiles.api.domain.ProfilesRepository

object ProfilesRepositoryStub : ProfilesRepository {
    private val profileStub =
        Profile("", null, null, null, 0f, null)

    override suspend fun createProfile(profile: Profile) = Unit

    override suspend fun fetchCurrentProfile() = profileStub

    override suspend fun clearCachedUserProfile() = Unit

    override suspend fun observeCurrentProfile(reload: Boolean): Flow<Profile> = emptyFlow()

    override suspend fun updateCurrentProfileAvatar(
        avatarBytes: ByteArray,
        extension: String
    ) = Unit

    override suspend fun increaseProfileTotalPoints(points: Float) = Unit

    override suspend fun observeLeaderboard() = emptyFlow<List<Profile>>()
}
