package gradle.model.gradle.publish.repository

import gradle.serialization.serializer.JsonContentPolymorphicSerializer
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.artifacts.dsl.RepositoryHandler

@Serializable(with = ArtifactRepositorySerializer::class)
internal interface ArtifactRepository {

    /**
     * Sets the name for this repository.
     *
     * If this repository is to be added to an [org.gradle.api.artifacts.ArtifactRepositoryContainer]
     * (including [RepositoryHandler]), its name cannot be changed after it has
     * been added to the container.
     *
     * @param name The name. Must not be null.
     * @throws IllegalStateException If the name is set after it has been added to the container.
     */
    val name: String?

    /**
     * Configures the content of this repository.
     * @param configureAction the configuration action
     *
     * @since 5.1
     */
    val content: RepositoryContentDescriptor?

    fun applyTo(handler: RepositoryHandler)

    fun applyTo(repository: org.gradle.api.artifacts.repositories.ArtifactRepository) {
        name?.let(repository::setName)
        content?.let { content ->
            repository.content(content::applyTo)
        }
    }
}

private object ArtifactRepositorySerializer : JsonContentPolymorphicSerializer<ArtifactRepository>(
    ArtifactRepository::class,
)

internal object ArtifactRepositoryTransformingSerializer : KeyTransformingSerializer<ArtifactRepository>(
    ArtifactRepository.serializer(),
    "type",
)
