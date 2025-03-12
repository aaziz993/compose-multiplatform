package gradle.plugins.publish

import gradle.plugins.Buildable
import org.gradle.api.publish.PublicationArtifact

/**
 * An artifact published as part of a [Publication].
 *
 * @since 4.8
 */
internal interface PublicationArtifact : Buildable {

    /**
     * Registers some tasks which build this artifact.
     *
     * @param tasks The tasks. These are evaluated as per [org.gradle.api.Task.dependsOn].
     */
    val builtBy: List<String>?

    override fun applyTo(buildable: org.gradle.api.Buildable) {
        buildable as PublicationArtifact

        builtBy?.let { builtBy ->
            buildable.builtBy(*builtBy.toTypedArray())
        }
    }
}
