package gradle.model

import org.gradle.api.Buildable

/**
 * A `Buildable` represents an artifact or set of artifacts which are built by one or more [Task]
 * instances.
 */
internal interface Buildable {

    fun applyTo(buildable: Buildable)
}
