import dependencies.AppDependencies

plugins {
    id(AppPlugins.androidLibrary)
    id(AppPlugins.androidKotlin)
}

android {
    namespace = "ru.abramovkirill.languageapp.profile.impl"

    compileSdk = AppConfig.Sdk.compile

    defaultConfig {
        minSdk = AppConfig.Sdk.min
        testInstrumentationRunner = AppConfig.testInstrumentationRunner
    }

    val buildConfigFields = AppConfig.buildConfigFields(project)

    buildTypes {
        release {
            manifestPlaceholders.putAll(
                buildConfigFields.associate { field ->
                    field.name to field.releaseValue
                }
            )
        }

        debug {
            manifestPlaceholders.putAll(
                buildConfigFields.associate { field ->
                    field.name to field.debugValue
                }
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = AppConfig.jvmTarget
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = AppConfig.composeKotlinCompilerExtensionVersion
    }
}

dependencies {
    modules(
        ":features:profile:api",
        ":features:auth:api",
        ":core:env:api",
        ":core:navigation:api",
        ":core:navigation:compose-impl",
        ":core:design",
        ":core:data:supabase",
        ":core:data:profiles:api",
        ":core:coroutines:dispatchers",
        ":common:utils",
    )

    dependencies(
        AppDependencies.immutableCollections,
        AppDependencies.AndroidX.appCompat,
        AppDependencies.Koin.bom,
        AppDependencies.Koin.core,
        AppDependencies.Orbit.viewModel,
        AppDependencies.Orbit.compose,
        AppDependencies.Coroutines.core,
        AppDependencies.Coroutines.android,
        AppDependencies.Coil.compose,
        AppDependencies.Compose.bom,
        AppDependencies.Compose.material,
        AppDependencies.Compose.viewModel,
        AppDependencies.Compose.navigation,
        AppDependencies.Compose.lifeCycleRuntime,
        AppDependencies.Compose.preview,
        AppDependencies.Accompanist.permissions,
    )

    debugDependencies(
        AppDependencies.Compose.debugPreview,
    )
}