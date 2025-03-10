package gradle.model.gradle.publishing

import kotlinx.serialization.Serializable
import org.gradle.api.artifacts.repositories.ArtifactRepository

@Serializable
internal data class ArtifactRepository(
    /**
     * Sets the name for this repository.
     *
     * If this repository is to be added to an [org.gradle.api.artifacts.ArtifactRepositoryContainer]
     * (including [org.gradle.api.artifacts.dsl.RepositoryHandler]), its name cannot be changed after it has
     * been added to the container.
     *
     * @param name The name. Must not be null.
     * @throws IllegalStateException If the name is set after it has been added to the container.
     */
    val name: String? = null,
    /**
     * Configures the content of this repository.
     * @param configureAction the configuration action
     *
     * @since 5.1
     */
    val content: RepositoryContentDescriptor? = null,
) {

    fun applyTo(repository: ArtifactRepository) {
        name?.let(repository::setName)

        content?.let { content ->
            repository.content(content::applyTo)
        }
    }
}
