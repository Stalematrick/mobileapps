@file:Suppress("UnstableApiUsage")

import dependencies.AppDependencies

plugins {
    id(AppPlugins.androidApplication)
    id(AppPlugins.androidKotlin)
    id(AppPlugins.kotlinKapt)
}

android {
    namespace = AppConfig.applicationId
    compileSdk = AppConfig.Sdk.compile

    androidResources {
        generateLocaleConfig = true
    }

    defaultConfig {
        applicationId = AppConfig.applicationId
        minSdk = AppConfig.Sdk.min
        targetSdk = AppConfig.Sdk.target
        versionCode = AppConfig.Version.code
        versionName = AppConfig.Version.name

        testInstrumentationRunner = AppConfig.testInstrumentationRunner
    }

    val buildConfigFields = AppConfig.buildConfigFields(project)

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("debug")

            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

            buildConfigFields.forEach { field ->
                buildConfigField(field.type, field.name, "\"${field.releaseValue}\"")
            }

            manifestPlaceholders.putAll(
                buildConfigFields.associate { field ->
                    field.name to field.releaseValue
                }
            )
        }

        debug {
            buildConfigFields.forEach { field ->
                buildConfigField(field.type, field.name, "\"${field.debugValue}\"")
            }

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
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = AppConfig.composeKotlinCompilerExtensionVersion
    }
}

dependencies {
    modules(
        ":common:utils",
        ":core:design",
        ":core:navigation:api",
        ":core:navigation:compose-impl",
        ":core:coroutines:dispatchers",
        ":core:coroutines:scopes",
        ":core:logger:api",
        ":core:logger:android",
        ":core:env:api",
        ":core:env:android",
        ":core:data:http-client",
        ":core:data:serialization",
        ":core:data:supabase",
        ":core:data:profiles:api",
        ":core:data:profiles:impl",
        ":features:onboarding:api",
        ":features:onboarding:compose-impl",
        ":features:auth:api",
        ":features:auth:compose-impl",
        ":features:profile:api",
        ":features:profile:compose-impl",
        ":features:main:api",
        ":features:main:compose-impl",
        ":features:exercises:guess-animal:api",
        ":features:exercises:guess-animal:compose-impl",
        ":features:exercises:word-practice:api",
        ":features:exercises:word-practice:compose-impl",
        ":features:exercises:audition:api",
        ":features:exercises:audition:compose-impl",
    )

    dependencies(
        AppDependencies.immutableCollections,
        AppDependencies.AndroidX.appCompat,
        AppDependencies.Ktx.core,
        AppDependencies.Coroutines.core,
        AppDependencies.Coroutines.android,
        AppDependencies.Koin.bom,
        AppDependencies.Koin.core,
        AppDependencies.Orbit.viewModel,
        AppDependencies.Orbit.compose,
        AppDependencies.Coil.compose,
        AppDependencies.Coil.gif,
        AppDependencies.Compose.bom,
        AppDependencies.Compose.material,
        AppDependencies.Compose.lifeCycleRuntime,
        AppDependencies.Compose.activity,
        AppDependencies.Compose.navigation,
        AppDependencies.Compose.preview,
        AppDependencies.Splash.core,
    )

    debugDependencies(
        AppDependencies.Compose.debugPreview,
    )

    testDependencies(
        AppDependencies.Testing.junit,
        AppDependencies.Koin.test,
    )
}