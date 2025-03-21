package gradle.plugins.kotlin

import gradle.project.Dependency
import gradle.project.DependencyTransformingSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.HasKotlinDependencies

/**
 * Contains all the configurable Kotlin dependencies for a Kotlin DSL entity, like an instance of `KotlinSourceSet`.
 */
internal interface HasKotlinDependencies<T : HasKotlinDependencies> {

    /**
     * Configures all dependencies for this entity.
     */
    val dependencies: List<@Serializable(with = DependencyTransformingSerializer::class) Dependency>?

    context(Project)
    fun applyTo(recipient: T)
}
