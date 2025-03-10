package gradle.model.gradle.publish

import kotlinx.serialization.Serializable
import org.gradle.api.publish.maven.MavenPomContributorSpec

/**
 * Allows to add contributors of a Maven publication.
 *
 * @since 4.8
 * @see MavenPom
 *
 * @see MavenPomContributor
 */
@Serializable
internal data class MavenPomContributorSpec(
    /**
     * Creates, configures and adds a contributor to the publication.
     */
    val contributor: MavenPomContributor? = null,
) {

    fun applyTo(spec: MavenPomContributorSpec) {
        contributor?.let { contributor ->
            spec.contributor(contributor::applyTo)
        }
    }
}
