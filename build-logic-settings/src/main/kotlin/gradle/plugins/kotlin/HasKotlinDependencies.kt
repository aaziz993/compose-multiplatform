package gradle.plugins.kotlin

import gradle.plugins.project.Dependency
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.HasKotlinDependencies

/**
 * Contains all the configurable Kotlin dependencies for a Kotlin DSL entity, like an instance of `KotlinSourceSet`.
 */
internal interface HasKotlinDependencies<T : HasKotlinDependencies> {

    /**
     * Configures all dependencies for this entity.
     */
    val dependencies: Set<Dependency>?

    context(Project)
    fun applyTo(receiver: T) {
        dependencies?.let { dependencies ->
            receiver.dependencies {
                dependencies.forEach { dependency ->
                    dependency.applyTo(this)
                }
            }
        }
    }
}
