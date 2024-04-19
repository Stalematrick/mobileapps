import dependencies.AppDependencies

plugins {
    id(AppPlugins.javaLibrary)
    id(AppPlugins.jvmKotlin)
}

dependencies {
    dependencies(
        AppDependencies.kotlinXSerialization,
        AppDependencies.Koin.bom,
        AppDependencies.Koin.core,
    )
}