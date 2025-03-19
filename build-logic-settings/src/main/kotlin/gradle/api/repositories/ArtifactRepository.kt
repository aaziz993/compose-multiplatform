package gradle.api.repositories

import gradle.api.Named
import gradle.isUrl
import gradle.serialization.serializer.BaseKeyTransformingSerializer
import gradle.serialization.serializer.JsonPolymorphicSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.file.Directory
import org.gradle.api.initialization.Settings

@Serializable(with = ArtifactRepositorySerializer::class)
internal interface ArtifactRepository<T : org.gradle.api.artifacts.repositories.ArtifactRepository> : Named<T> {

    /**
     * Configures the content of this repository.
     * @param configureAction the configuration action
     *
     * @since 5.1
     */
    val content: RepositoryContentDescriptorImpl?

    context(Settings)
    @Suppress("UnstableApiUsage")
    override fun applyTo(named: T) = with(layout.settingsDirectory) {
        _applyTo(named)
    }

    context(Project)
    override fun applyTo(named: T) = with(layout.projectDirectory) {
        _applyTo(named)
    }

    context(Directory)
    fun _applyTo(named: T) {
        content?.let { content ->
            named.content(content::applyTo)
        }
    }

    context(Settings)
    fun applyTo(handler: RepositoryHandler)

    context(Project)
    fun applyTo(handler: RepositoryHandler)
}

private object ArtifactRepositorySerializer : JsonPolymorphicSerializer<ArtifactRepository<*>>(
    ArtifactRepository::class,
)

internal object ArtifactRepositoryTransformingSerializer : BaseKeyTransformingSerializer<ArtifactRepository<*>>(
    ArtifactRepositorySerializer,
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
