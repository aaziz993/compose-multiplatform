package gradle.model.repository

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.ExclusiveContentRepository

/**
 * Describes one or more repositories which together constitute the only possible
 * source for an artifact, independently of the others.
 *
 * This means that if a repository declares an include, other repositories will
 * automatically exclude it.
 *
 * @since 6.2
 */
@Serializable
internal data class ExclusiveContentRepository(
    /**
     * Declares the repository
     * @param repositories the repositories for which we declare exclusive content
     * @return this repository descriptor
     */
    val forRepositories: List<String>? = null,

    /**
     * Defines the content filter for this repository
     * @param config the configuration of the filter
     * @return this repository descriptor
     */
    val filter: InclusiveRepositoryContentDescriptorImpl? = null,
) {

    context(Project)
    fun applyTo(repository: ExclusiveContentRepository) {
        forRepositories?.map(repositories::getByName)?.let { forRepositories ->
            repository.forRepositories(*forRepositories.toTypedArray())
        }

        filter?.let { filter ->
            repository.filter(filter::applyTo)
        }
    }
}
