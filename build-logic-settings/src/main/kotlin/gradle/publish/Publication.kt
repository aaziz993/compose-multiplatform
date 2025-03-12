package gradle.publish

import gradle.accessors.publishing
import gradle.api.Named
import gradle.serialization.serializer.JsonContentPolymorphicSerializer
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project

/**
 * A publication is a description of a consumable representation of one or more artifacts, and possibly associated metadata.
 *
 * @since 1.3
 */
@Serializable(with = PublicationSerializer::class)
internal interface Publication : Named {

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
    override fun applyTo(named: org.gradle.api.Named) {
        named as org.gradle.api.publish.Publication

        withoutBuildIdentifier?.takeIf { it }?.run { named.withoutBuildIdentifier() }
        withBuildIdentifier?.takeIf { it }?.run { named.withBuildIdentifier() }
    }

    context(Project)
    override fun applyTo() =
        super.applyTo(publishing.publications)
}

private object PublicationSerializer : JsonContentPolymorphicSerializer<Publication>(
    Publication::class,
)

internal object PublicationTransformingSerializer : KeyTransformingSerializer<Publication>(
    Publication.serializer(),
    "type",
)
