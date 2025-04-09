package gradle.api.publish

import gradle.api.ProjectNamed
import klib.data.type.reflection.trySet
import klib.data.type.serialization.json.serializer.ReflectionMapTransformingPolymorphicSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project

/**
 * A publication is a description of a consumable representation of one or more artifacts, and possibly associated metadata.
 *
 * @since 1.3
 */
@Serializable(with = ReflectionPublicationMapTransformingPolymorphicSerializer::class)
internal interface Publication<T : org.gradle.api.publish.Publication> : ProjectNamed<T> {

    /**
     * Disables publication of a unique build identifier in Gradle Module Metadata.
     *
     *
     * The build identifier is not published by default.
     *
     * @since 6.6
     */
    val withoutBuildIdentifier: Boolean?

    /**
     * Enables publication of a unique build identifier in Gradle Module Metadata.
     *
     *
     * The build identifier is not published by default.
     *
     * @since 6.6
     */
    val withBuildIdentifier: Boolean?

    context(Project)
    override fun applyTo(receiver: T) {
        receiver::withoutBuildIdentifier trySet withoutBuildIdentifier
        receiver::withBuildIdentifier trySet withBuildIdentifier
    }

    context(Project)
    fun applyTo()
}

private class ReflectionPublicationMapTransformingPolymorphicSerializer(serializer: KSerializer<Nothing>)
    : ReflectionMapTransformingPolymorphicSerializer<Publication<*>>(
    Publication::class,
)
