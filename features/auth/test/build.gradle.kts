import dependencies.AppDependencies

plugins {
    id(AppPlugins.javaLibrary)
    id(AppPlugins.jvmKotlin)
}

dependencies {
    modules(
        ":features:auth:api",
    )

    dependencies(
        AppDependencies.Coroutines.core,
    )
}