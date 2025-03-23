package gradle.api.publish.maven

import gradle.api.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.publish.maven.MavenPomOrganization

/**
 * The organization of a Maven publication.
 *
 * @since 4.8
 * @see MavenPom
 */
@Serializable
internal data class MavenPomOrganization(
    /**
     * The name of this organization.
     */
    val name: String? = null,
    /**
     * The URL of this organization.
     */
    val url: String? = null,
) {

    fun applyTo(receiver: MavenPomOrganization) {
        organization.name tryAssign name
        organization.url tryAssign url
    }
}
