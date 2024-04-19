package ru.abramovkirill.languageapp.core.profiles.impl.di

import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import ru.abramovkirill.languageapp.core.coroutines.dispatchers.di.IoCoroutineDispatcherQualifier
import ru.abramovkirill.languageapp.core.data.cache.InMemoryCacheContainer
import ru.abramovkirill.languageapp.core.profiles.api.domain.Profile
import ru.abramovkirill.languageapp.core.profiles.api.domain.ProfilesRepository
import ru.abramovkirill.languageapp.core.profiles.impl.data.ProfilesRepositoryImpl

private val ProfileInMemoryCacheContainerQualifier =
    qualifier("ProfileInMemoryCacheContainer")

val profilesModule = module {
    single<InMemoryCacheContainer<Profile>>(ProfileInMemoryCacheContainerQualifier) {
        InMemoryCacheContainer(
            cacheLifeTime = Long.MAX_VALUE
        )
    }

    single<ProfilesRepository> {
        ProfilesRepositoryImpl(
            dispatcher = get(IoCoroutineDispatcherQualifier),
            supabaseClient = get(),
            inMemoryUserProfileCacheContainer = get(ProfileInMemoryCacheContainerQualifier),
            environmentConfig = get(),
        )
    }
}
