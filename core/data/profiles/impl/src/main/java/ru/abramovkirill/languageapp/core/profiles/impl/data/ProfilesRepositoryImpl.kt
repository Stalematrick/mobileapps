package ru.abramovkirill.languageapp.core.profiles.impl.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import ru.abramovkirill.languageapp.core.data.cache.InMemoryCacheContainer
import ru.abramovkirill.languageapp.core.env.EnvironmentConfig
import ru.abramovkirill.languageapp.core.profiles.api.domain.Profile
import ru.abramovkirill.languageapp.core.profiles.api.domain.ProfilesRepository
import ru.abramovkirill.languageapp.core.profiles.impl.mappers.toData
import ru.abramovkirill.languageapp.core.profiles.impl.mappers.toDomain

internal class ProfilesRepositoryImpl(
    private val dispatcher: CoroutineDispatcher,
    private val supabaseClient: SupabaseClient,
    private val inMemoryUserProfileCacheContainer: InMemoryCacheContainer<Profile>,
    private val environmentConfig: EnvironmentConfig,
) : ProfilesRepository {
    override suspend fun createProfile(profile: Profile): Unit =
        withContext(dispatcher) {
            supabaseClient.postgrest.from("profiles")
                .upsert(
                    value = profile.toData(),
                    onConflict = "id",
                    ignoreDuplicates = true
                )
        }

    override suspend fun fetchCurrentProfile(): Profile =
        withContext(dispatcher) {
            val currentUserInfo = supabaseClient.auth.currentUserOrNull()
                ?: throw IllegalStateException("No current user found")

            val profileData = supabaseClient.postgrest.from("profiles")
                .select {
                    filter {
                        eq("id", currentUserInfo.id)
                    }
                }
                .decodeSingle<ProfileData>()

            profileData.toDomain()
        }

    override suspend fun clearCachedUserProfile() {
        inMemoryUserProfileCacheContainer.clear()
    }

    override suspend fun observeCurrentProfile(reload: Boolean): Flow<Profile> {
        if (reload || inMemoryUserProfileCacheContainer.isExpired) {
            inMemoryUserProfileCacheContainer.value = fetchCurrentProfile()
        }

        return inMemoryUserProfileCacheContainer.valueFlow
            .filterNotNull()
    }

    override suspend fun observeLeaderboard(): Flow<List<Profile>> {
        val channel = supabaseClient.realtime
            .channel("profiles_leaderboard")

        val leaderboardFlow = channel
            .postgresChangeFlow<PostgresAction>(schema = "public") {
                table = "profiles"
            }
            .map { fetchLeaderboard() }
            .onStart { emit(fetchLeaderboard()) }
            .distinctUntilChanged()

        channel.subscribe()

        return leaderboardFlow
    }

    override suspend fun updateCurrentProfileAvatar(
        avatarBytes: ByteArray,
        extension: String,
    ): Unit =
        withContext(dispatcher) {
            val profile = fetchCurrentProfile()

            val previousAvatarsPaths = supabaseClient.storage
                .from("avatars")
                .list(profile.id)
                .map { file -> "${profile.id}/${file.name}" }

            supabaseClient.storage.from("avatars")
                .delete(previousAvatarsPaths)

            val avatarFileName =
                "avatar_${Clock.System.now().toEpochMilliseconds()}.${extension}"

            val path = supabaseClient.storage.from("avatars")
                .upload(
                    path = "${profile.id}/$avatarFileName",
                    data = avatarBytes,
                    upsert = true,
                )

            val avatarUrl = "${environmentConfig.supabaseStorageUrl}/$path"

            val updatedProfile = profile.copy(avatarUrl = avatarUrl)

            supabaseClient.postgrest.from("profiles")
                .update(
                    value = updatedProfile.toData()
                ) {
                    filter {
                        eq("id", profile.id)
                    }
                }

            inMemoryUserProfileCacheContainer.value = updatedProfile
        }

    override suspend fun increaseProfileTotalPoints(points: Float) {
        val profile = fetchCurrentProfile()

        val updatedProfile = profile.copy(totalScore = profile.totalScore + points)

        supabaseClient.postgrest.from("profiles")
            .update(
                value = updatedProfile.toData()
            ) {
                filter {
                    eq("id", profile.id)
                }
            }

        inMemoryUserProfileCacheContainer.value = updatedProfile
    }

    private suspend fun fetchLeaderboard(): List<Profile> =
        supabaseClient.postgrest
            .from("profiles_leaderboard")
            .select()
            .decodeList<ProfileData>()
            .map(ProfileData::toDomain)
}
