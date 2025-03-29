package gradle.api.repositories

import gradle.api.Named
import gradle.serialization.serializer.JsonBaseObjectTransformingContentPolymorphicSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.file.Directory
import org.gradle.api.initialization.Settings

@Serializable(with = ArtifactRepositoryObjectTransformingContentPolymorphicSerializer::class)
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
    override fun applyTo(receiver: T) {
    }

    context(Project)
    override fun applyTo(receiver: T) = with(project.layout.projectDirectory) {
        _applyTo(receiver)
    }

    context(Directory)
    fun _applyTo(receiver: T) {
    }

    context(Settings)
    fun applyTo(receiver: RepositoryHandler)

    context(Project)
    fun applyTo(receiver: RepositoryHandler)
}

private class ArtifactRepositoryObjectTransformingContentPolymorphicSerializer(serializer: KSerializer<Nothing>)
    : JsonBaseObjectTransformingContentPolymorphicSerializer<ArtifactRepository<*>>(
    ArtifactRepository::class,
) {

    override fun transformDeserialize(key: String, value: JsonElement?): JsonObject =
        JsonObject(
            buildMap {
                value?.let { value ->
                    put("type", JsonPrimitive(key))
                    if (value is JsonPrimitive) put("url", value)
                    else putAll(value.jsonObject)
                } ?: run {
                    put("type", JsonPrimitive("maven"))
                    put("url", JsonPrimitive(key))
                }
            },
        )
}
