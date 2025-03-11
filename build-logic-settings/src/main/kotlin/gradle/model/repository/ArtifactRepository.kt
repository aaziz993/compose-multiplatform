package gradle.model.repository

import gradle.maybeNamed
import gradle.model.project.Dependency
import gradle.model.project.SUB_CONFIGURATIONS
import gradle.serialization.serializer.BaseKeyTransformingSerializer
import gradle.serialization.serializer.JsonContentPolymorphicSerializer
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.Project
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

    context(Project)
    fun applyTo()

    fun applyTo(repository: org.gradle.api.artifacts.repositories.ArtifactRepository) {
        repository.name = name
        content?.let { content ->
            repository.content(content::applyTo)
        }
    }

    context(Project)
    fun applyTo(
        named: NamedDomainObjectCollection<out org.gradle.api.artifacts.repositories.ArtifactRepository>,
        createAndConfigure: ((org.gradle.api.artifacts.repositories.ArtifactRepository.() -> Unit) -> org.gradle.api.artifacts.repositories.ArtifactRepository)?
    ) = named.configure(
        name, createAndConfigure,
    ) {
        applyTo(this)
    }
}

private object ArtifactRepositorySerializer : JsonContentPolymorphicSerializer<ArtifactRepository>(
    ArtifactRepository::class,
)

internal object ArtifactRepositoryTransformingSerializer : BaseKeyTransformingSerializer<Dependency>(
    Dependency.serializer(),
) {

    override fun transformKey(key: String, value: JsonElement?): JsonObject = JsonObject(
        buildMap {
            when {
                value == null -> {
                    put("type", JsonPrimitive("maven"))
                    put("url", JsonPrimitive(key))
                }

                else -> {
                    put("type", JsonPrimitive(key))
                }
            }
        },
    )

    override fun transformValue(key: String, value: String): JsonObject = JsonObject(
        mapOf(
            "url" to JsonPrimitive(value),
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
