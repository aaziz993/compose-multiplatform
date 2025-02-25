package plugin.project.kotlin.model;

import plugin.model.dependency.ProjectDependency;

/**
 * Contains all the configurable Kotlin dependencies for a Kotlin DSL entity, like an instance of `KotlinSourceSet`.
 */
internal interface HasKotlinDependencies {

    /**
     * Configures all dependencies for this entity.
     */
    val dependencies: List<ProjectDependency>?
}
