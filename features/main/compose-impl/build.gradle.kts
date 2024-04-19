import dependencies.AppDependencies

plugins {
    id(AppPlugins.androidLibrary)
    id(AppPlugins.androidKotlin)
}

android {
    namespace = "ru.abramovkirill.languageapp.main.impl"

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

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = AppConfig.composeKotlinCompilerExtensionVersion
    }
}

dependencies {
    modules(
        ":features:main:api",
        ":features:profile:api",
        ":features:exercises:guess-animal:api",
        ":features:exercises:word-practice:api",
        ":features:exercises:audition:api",
        ":core:navigation:api",
        ":core:navigation:compose-impl",
        ":core:design",
        ":core:data:profiles:api",
        ":common:utils",
    )

    dependencies(
        AppDependencies.immutableCollections,
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
    )

    debugDependencies(
        AppDependencies.Compose.debugPreview,
    )
}