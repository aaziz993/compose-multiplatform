package gradle.api.publish

import gradle.api.ProjectNamed
import gradle.serialization.serializer.JsonPolymorphicSerializer
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project

/**
 * A publication is a description of a consumable representation of one or more artifacts, and possibly associated metadata.
 *
 * @since 1.3
 */
@Serializable(with = PublicationSerializer::class)
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
        withoutBuildIdentifier?.takeIf { it }?.run { receiver.withoutBuildIdentifier() }
        withBuildIdentifier?.takeIf { it }?.run { receiver.withBuildIdentifier() }
    }

    context(Project)
    fun applyTo()
}

private object PublicationSerializer : JsonPolymorphicSerializer<Publication<*>>(
    Publication::class,
)

internal object PublicationTransformingSerializer : KeyTransformingSerializer<Publication<*>>(
    PublicationSerializer,
    "type",
)
