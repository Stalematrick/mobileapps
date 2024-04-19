// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id(AppPlugins.androidApplication) version AppPlugins.Versions.androidApplication apply false
    id(AppPlugins.androidKotlin) version AppPlugins.Versions.androidKotlin apply false
    id(AppPlugins.serialization) version AppPlugins.Versions.androidKotlin apply false
    id(AppPlugins.detekt) version AppPlugins.Versions.detekt
    id(AppPlugins.jvmKotlin) version AppPlugins.Versions.jvmKotlin apply false
}

allprojects {
    apply(plugin =AppPlugins.detekt)

    detekt {
        buildUponDefaultConfig = true
        allRules = false
        autoCorrect = false
        config.setFrom("${rootProject.rootDir}/detekt-config.yml")
    }

    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        jvmTarget = AppConfig.jvmTarget
    }

    tasks.withType<io.gitlab.arturbosch.detekt.DetektCreateBaselineTask>().configureEach {
        jvmTarget = AppConfig.jvmTarget
    }
}