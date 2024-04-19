import dependencies.AppDependencies

plugins {
    id(AppPlugins.androidLibrary)
    id(AppPlugins.androidKotlin)
}

android {
    namespace = "ru.abramovkirill.languageapp.core.data.supabase"

    compileSdk = AppConfig.Sdk.compile

    defaultConfig {
        minSdk = AppConfig.Sdk.min
        testInstrumentationRunner = AppConfig.testInstrumentationRunner
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = AppConfig.jvmTarget
    }
}

dependencies {
    modules(
        ":core:env:api",
    )

    dependencies(
        AppDependencies.Koin.bom,
        AppDependencies.Koin.core,
    )

    apiDependencies(
        AppDependencies.Supabase.bom,
        AppDependencies.Supabase.auth,
        AppDependencies.Supabase.composeAuth,
        AppDependencies.Supabase.postgrest,
        AppDependencies.Supabase.storage,
        AppDependencies.Supabase.realtime,
    )
}