package gradle.api.repositories

import gradle.api.Named
import klib.data.type.reflection.tryApply
import klib.data.type.serialization.serializer.ReflectionBaseMapTransformingPolymorphicSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.file.Directory
import org.gradle.api.initialization.Settings

@Serializable(with = ReflectionArtifactRepositoryMapTransformingPolymorphicSerializer::class)
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
    override fun applyTo(receiver: T) = with(settings.layout.settingsDirectory) {
        _applyTo(receiver)
    }

    context(Project)
    override fun applyTo(receiver: T) = with(project.layout.projectDirectory) {
        _applyTo(receiver)
    }

    context(Directory)
    fun _applyTo(receiver: T) {
        receiver::content tryApply content?.let { content -> content::applyTo }
    }

    context(Settings)
    fun applyTo(receiver: RepositoryHandler)

    context(Project)
    fun applyTo(receiver: RepositoryHandler)
}

private class ReflectionArtifactRepositoryMapTransformingPolymorphicSerializer(serializer: KSerializer<Nothing>)
    : ReflectionBaseMapTransformingPolymorphicSerializer<ArtifactRepository<*>>(
    ArtifactRepository::class,
) {

    override fun transformDeserialize(key: String, value: Any?): Map<String, Any?> =
        buildMap {
            value?.let { value ->
                put("type", key)
                if (value is String) put("url", value)
            } ?: run {
                put("type", "maven")
                put("url", key)
            }
        }
}
