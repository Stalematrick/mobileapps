import dependencies.AppDependencies

plugins {
    id(AppPlugins.javaLibrary)
    id(AppPlugins.jvmKotlin)
}

dependencies {
    modules(
        ":core:navigation:api",
    )

    dependencies(
        AppDependencies.Coroutines.core,
    )
}