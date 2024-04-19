import dependencies.AppDependencies

plugins {
    id(AppPlugins.androidLibrary)
    id(AppPlugins.androidKotlin)
}

android {
    namespace = "ru.abramovkirill.languageapp.core.design"

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
        ":common:utils",
    )

    dependencies(
        AppDependencies.immutableCollections,
        AppDependencies.AndroidX.appCompat,
        AppDependencies.Ktx.core,
        AppDependencies.Splash.core,
        AppDependencies.Coil.compose,
        AppDependencies.Compose.bom,
        AppDependencies.Compose.material,
        AppDependencies.Compose.preview,
        AppDependencies.Compose.navigation,
        AppDependencies.Compose.activity,
    )

    debugDependencies(
        AppDependencies.Compose.debugPreview,
    )
}