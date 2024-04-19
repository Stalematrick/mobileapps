package ru.abramovkirill.languageapp.core.env

import ru.abramovkirill.languageapp.core.env.android.BuildConfig

class EnvironmentConfigAndroid : EnvironmentConfig {
    override val applicationId = BuildConfig.APPLICATION_ID
    override val supabaseAnonKey = BuildConfig.SUPABASE_ANON_KEY
    override val supabaseUrl = BuildConfig.SUPABASE_URL
    override val supabaseStorageUrl = BuildConfig.SUPABASE_STORAGE_URL
    override val googleServerClientId = BuildConfig.GOOGLE_SERVER_CLIENT_ID
    override val supabaseRedirectScheme = BuildConfig.SUPABASE_REDIRECT_SCHEME
    override val supabaseRedirectHost = BuildConfig.SUPABASE_REDIRECT_HOST
}
