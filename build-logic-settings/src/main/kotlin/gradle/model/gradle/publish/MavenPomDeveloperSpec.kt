package gradle.model.gradle.publish

import kotlinx.serialization.Serializable
import org.gradle.api.publish.maven.MavenPomDeveloperSpec

/**
 * Allows to add developers to a Maven publication.
 *
 * @since 4.8
 * @see MavenPom
 *
 * @see MavenPomDeveloper
 */
@Serializable
internal data class MavenPomDeveloperSpec(
    /**
     * Creates, configures and adds a developer to the publication.
     */
    val developer: MavenPomDeveloper? = null,
) {

    fun applyTo(spec: MavenPomDeveloperSpec) {
        developer?.let { developer ->
            spec.developer(developer::applyTo)
        }
    }
}
