import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project
import dependencies.Dependency
import dependencies.notationWithVersion

fun DependencyHandler.modules(vararg names: String) {
    names.forEach { name ->
        add("implementation", project(name))
    }
}

fun DependencyHandler.androidTestModules(vararg names: String) {
    names.forEach { name ->
        add("androidTestImplementation", project(name))
    }
}

fun DependencyHandler.dependencies(vararg dependencies: Dependency) {
    dependencies.forEach { dependency ->
        addDependency("implementation", dependency)
    }
}

fun DependencyHandler.apiDependencies(vararg dependencies: Dependency) {
    dependencies.forEach { dependency ->
        addDependency("api", dependency)
    }
}

fun DependencyHandler.kaptDependencies(vararg dependencies: Dependency) {
    dependencies.forEach { dependency ->
        addDependency("kapt", dependency)
    }
}

fun DependencyHandler.debugDependencies(vararg dependencies: Dependency) {
    dependencies.forEach { dependency ->
        addDependency("debugImplementation", dependency)
    }
}

fun DependencyHandler.testDependencies(vararg dependencies: Dependency) {
    dependencies.forEach { dependency ->
        addDependency("testImplementation", dependency)
    }
}

fun DependencyHandler.androidTestDependencies(vararg dependencies: Dependency) {
    dependencies.forEach { dependency ->
        addDependency("androidTestImplementation", dependency)
    }
}

private fun DependencyHandler.addDependency(
    configurationName: String,
    dependency: Dependency,
) {
    val notation = dependency.notationWithVersion
    add(configurationName, if (dependency.isPlatform) platform(notation) else notation)
}

