package plugin.project.kotlin.model

import kotlinx.serialization.Serializable
import plugin.model.dependency.Dependency
import plugin.model.dependency.DependencyTransformingSerializer

/**
 * Contains all the configurable Kotlin dependencies for a Kotlin DSL entity, like an instance of `KotlinSourceSet`.
 */
internal interface HasKotlinDependencies {

    /**
     * Configures all dependencies for this entity.
     */
    val dependencies: List<@Serializable(with = DependencyTransformingSerializer::class) Dependency>?
}
