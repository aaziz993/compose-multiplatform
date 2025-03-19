package gradle.api.publish

import gradle.api.Buildable
import java.util.SortedSet
import org.gradle.api.publish.PublicationArtifact

/**
 * An artifact published as part of a [Publication].
 *
 * @since 4.8
 */
internal interface PublicationArtifact<T: PublicationArtifact> : Buildable<T> {

    /**
     * Registers some tasks which build this artifact.
     *
     * @param tasks The tasks. These are evaluated as per [org.gradle.api.Task.dependsOn].
     */
    val builtBy: Set<String>?

    override fun applyTo(buildable: T) {
        builtBy?.let { builtBy ->
            buildable.builtBy(*builtBy.toTypedArray())
        }
    }
}
