import dependencies.AppDependencies

plugins {
    id(AppPlugins.javaLibrary)
    id(AppPlugins.jvmKotlin)
}

dependencies {
    modules(
        ":core:logger:api",
    )

    dependencies(
        AppDependencies.Ktor.core,
        AppDependencies.Ktor.engine,
        AppDependencies.Ktor.logging,
        AppDependencies.Ktor.serialization,
        AppDependencies.Ktor.negotiation,
        AppDependencies.Koin.bom,
        AppDependencies.Koin.core,
    )
}