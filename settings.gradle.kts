pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "LanguageApp"

include(
    ":app",
    ":common:utils",
    ":core:design",
    ":core:navigation:api",
    ":core:navigation:compose-impl",
    ":core:navigation:compose-test",
    ":core:coroutines:dispatchers",
    ":core:coroutines:scopes",
    ":core:logger:api",
    ":core:logger:android",
    ":core:env:api",
    ":core:env:android",
    ":core:data:cache",
    ":core:data:http-client",
    ":core:data:serialization",
    ":core:data:supabase",
    ":core:data:profiles:api",
    ":core:data:profiles:impl",
    ":core:data:profiles:test",
    ":features:onboarding:api",
    ":features:onboarding:compose-impl",
    ":features:auth:api",
    ":features:auth:compose-impl",
    ":features:auth:test",
    ":features:main:api",
    ":features:main:compose-impl",
    ":features:profile:api",
    ":features:profile:compose-impl",
    ":features:exercises:guess-animal:api",
    ":features:exercises:guess-animal:compose-impl",
    ":features:exercises:word-practice:api",
    ":features:exercises:word-practice:compose-impl",
    ":features:exercises:audition:api",
    ":features:exercises:audition:compose-impl",
)
