import dependencies.AppDependencies

plugins {
    id(AppPlugins.androidLibrary)
    id(AppPlugins.androidKotlin)
}

android {
    namespace = "ru.abramovkirill.languageapp.onboarding.impl"

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
        ":features:onboarding:api",
        ":features:auth:api",
        ":core:navigation:api",
        ":core:navigation:compose-impl",
        ":core:design",
        ":core:coroutines:dispatchers",
        ":common:utils",
    )

    androidTestModules(
        ":core:navigation:compose-impl",
        ":core:navigation:compose-test",
        ":features:auth:compose-impl",
        ":features:auth:test",
        ":core:data:profiles:api",
        ":core:data:profiles:test",
    )

    dependencies(
        AppDependencies.Koin.bom,
        AppDependencies.Koin.core,
        AppDependencies.Orbit.viewModel,
        AppDependencies.Orbit.compose,
        AppDependencies.Coroutines.core,
        AppDependencies.Coroutines.android,
        AppDependencies.Compose.bom,
        AppDependencies.Compose.viewModel,
        AppDependencies.Compose.navigation,
        AppDependencies.Compose.lifeCycleRuntime,
        AppDependencies.Compose.material,
        AppDependencies.Compose.preview,
    )

    debugDependencies(
        AppDependencies.Compose.debugPreview,
        AppDependencies.Testing.composeManifest,
    )

    androidTestDependencies(
        AppDependencies.Compose.bom,
        AppDependencies.Testing.composeJunit,
        AppDependencies.Testing.mockitoCore,
        AppDependencies.Testing.mockitoKotlin,
        AppDependencies.Testing.composeNavigation,
        AppDependencies.Compose.navigation,
    )
}