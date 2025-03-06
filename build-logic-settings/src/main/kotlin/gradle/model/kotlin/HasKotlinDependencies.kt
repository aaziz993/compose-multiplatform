package gradle.model.kotlin

import kotlinx.serialization.Serializable
import gradle.model.project.Dependency
import gradle.model.project.DependencyTransformingSerializer

/**
 * Contains all the configurable Kotlin dependencies for a Kotlin DSL entity, like an instance of `KotlinSourceSet`.
 */
internal interface HasKotlinDependencies {

    /**
     * Configures all dependencies for this entity.
     */
    val dependencies: List<@Serializable(with = DependencyTransformingSerializer::class) Dependency>?
}
