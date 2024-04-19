package ru.abramovkirill.languageapp.core.data.supabase.di

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.FlowType
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.serializer.KotlinXSerializer
import io.github.jan.supabase.storage.Storage
import org.koin.dsl.module
import ru.abramovkirill.languageapp.core.env.EnvironmentConfig

val supabaseModule = module {
    single<SupabaseClient> {
        val environmentConfig = get<EnvironmentConfig>()

        createSupabaseClient(
            supabaseUrl = environmentConfig.supabaseUrl,
            supabaseKey = environmentConfig.supabaseAnonKey,
        ) {
            defaultSerializer = KotlinXSerializer(get())

            install(Auth) {
                flowType = FlowType.PKCE
                scheme = environmentConfig.supabaseRedirectScheme
                host = environmentConfig.supabaseRedirectHost
            }

            install(Postgrest)
            install(Storage)
            install(Realtime)
        }
    }
}
