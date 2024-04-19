import dependencies.AppDependencies

plugins {
    id(AppPlugins.androidLibrary)
    id(AppPlugins.androidKotlin)
    id(AppPlugins.serialization)
}

android {
    namespace = "ru.abramovkirill.languageapp.core.data.profiles.impl"

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
        ":core:coroutines:dispatchers",
        ":core:env:api",
        ":core:data:serialization",
        ":core:data:supabase",
        ":core:data:cache",
        ":core:data:profiles:api",
    )

    dependencies(
        AppDependencies.kotlinXSerialization,
        AppDependencies.Koin.bom,
        AppDependencies.Koin.core,
    )
}