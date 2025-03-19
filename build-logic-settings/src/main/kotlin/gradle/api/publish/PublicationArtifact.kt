package gradle.api.publish

import gradle.api.Buildable
import org.gradle.api.publish.PublicationArtifact

/**
 * An artifact published as part of a [Publication].
 *
 * @since 4.8
 */
internal interface PublicationArtifact<in T: PublicationArtifact> : Buildable<T> {

    /**
     * Registers some tasks which build this artifact.
     *
     * @param tasks The tasks. These are evaluated as per [org.gradle.api.Task.dependsOn].
     */
    val builtBy: Set<String>?

    override fun applyTo(recipient: T) {
        builtBy?.let { builtBy ->
            recipient.builtBy(*builtBy.toTypedArray())
        }
    }
}
