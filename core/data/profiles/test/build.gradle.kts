import dependencies.AppDependencies

plugins {
    id(AppPlugins.javaLibrary)
    id(AppPlugins.jvmKotlin)
}

dependencies {
    modules(
        ":core:data:profiles:api",
    )

    dependencies(
        AppDependencies.Coroutines.core,
    )
}