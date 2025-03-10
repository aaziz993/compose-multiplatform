package gradle.model.gradle.publish

import org.gradle.api.artifacts.repositories.ArtifactRepository

internal interface ArtifactRepository{
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
    val name: String
    /**
     * Configures the content of this repository.
     * @param configureAction the configuration action
     *
     * @since 5.1
     */
    val content: RepositoryContentDescriptor?

    fun toRepository(): ArtifactRepository

    fun applyTo(repository: ArtifactRepository) {
        content?.let { content ->
            repository.content(content::applyTo)
        }
    }
}
