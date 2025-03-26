package gradle.api.repositories

import org.gradle.api.artifacts.repositories.RepositoryLayout

/**
 * Represents the directory structure for a repository.
 *
 * @since 2.3 (feature was already present in Groovy DSL, this type introduced in 2.3)
 */
internal interface RepositoryLayout<T : RepositoryLayout> {

    fun applyTo(receiver: T)
}
