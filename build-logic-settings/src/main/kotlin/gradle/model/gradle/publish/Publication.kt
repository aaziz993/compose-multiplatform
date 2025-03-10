package gradle.model.gradle.publish

import gradle.model.Named
import org.gradle.api.publish.Publication

/**
 * A publication is a description of a consumable representation of one or more artifacts, and possibly associated metadata.
 *
 * @since 1.3
 */
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

    fun applyTo(publication: Publication) {
        withoutBuildIdentifier?.takeIf { it }?.run { publication.withoutBuildIdentifier() }
        withBuildIdentifier?.takeIf { it }?.run { publication.withBuildIdentifier() }
    }
}
