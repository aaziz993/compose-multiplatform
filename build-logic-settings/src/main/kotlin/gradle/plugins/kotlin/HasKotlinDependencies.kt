package gradle.plugins.kotlin

import gradle.plugins.project.Dependency
import gradle.plugins.project.DependencyTransformingSerializer
import kotlinx.serialization.Serializable

/**
 * Contains all the configurable Kotlin dependencies for a Kotlin DSL entity, like an instance of `KotlinSourceSet`.
 */
internal interface HasKotlinDependencies {

    /**
     * Configures all dependencies for this entity.
     */
    val dependencies: List<@Serializable(with = DependencyTransformingSerializer::class) Dependency>?
}
