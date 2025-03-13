package gradle.api.repositories

import gradle.api.maybeNamed
import gradle.isUrl
import gradle.serialization.serializer.BaseKeyTransformingSerializer
import gradle.serialization.serializer.JsonPolymorphicSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.gradle.api.NamedDomainObjectCollection
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
    val name: String

    /**
     * Configures the content of this repository.
     * @param configureAction the configuration action
     *
     * @since 5.1
     */
    val content: RepositoryContentDescriptor?

    fun applyTo(repository: org.gradle.api.artifacts.repositories.ArtifactRepository) {
        content?.let { content ->
            repository.content(content::applyTo)
        }
    }

    fun applyTo(
        named: NamedDomainObjectCollection<out org.gradle.api.artifacts.repositories.ArtifactRepository>,
        createAndConfigure: ((org.gradle.api.artifacts.repositories.ArtifactRepository.() -> Unit) -> org.gradle.api.artifacts.repositories.ArtifactRepository)?
    ) = named.configure(
        name, createAndConfigure,
    ) {
        applyTo(this)
    }

    fun applyTo(handler: RepositoryHandler)
}

private object ArtifactRepositorySerializer : JsonPolymorphicSerializer<ArtifactRepository>(
    ArtifactRepository::class,
)

internal object ArtifactRepositoryTransformingSerializer : BaseKeyTransformingSerializer<ArtifactRepository>(
    ArtifactRepository.serializer(),
) {

    override fun transformKey(key: String, value: JsonElement?): JsonObject = JsonObject(
        if (value == null && key.isUrl) mapOf(
            "type" to JsonPrimitive("maven"),
            "url" to JsonPrimitive(key),
        )
        else mapOf("type" to JsonPrimitive(key)),
    )

    override fun transformValue(key: String, value: JsonElement): JsonObject = JsonObject(
        mapOf(
            "url" to value,
        ),
    )
}

private inline fun <reified T> NamedDomainObjectCollection<out T>.configure(
    name: String,
    noinline createAndConfigure: ((T.() -> Unit) -> T)? = null,
    noinline configure: T.() -> Unit
) {
    if (name.isEmpty()) all(configure)
    else maybeNamed(name, configure) ?: createAndConfigure?.invoke(configure)
}
