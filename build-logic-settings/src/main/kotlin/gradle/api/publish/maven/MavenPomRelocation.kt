package gradle.api.publish.maven

import gradle.api.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.publish.maven.MavenPomRelocation

/**
 * The relocation information of a Maven publication that has been moved
 * to a new group and/or artifact ID.
 *
 * @since 4.8
 * @see MavenPom
 *
 * @see MavenPomDistributionManagement
 */
@Serializable
internal data class MavenPomRelocation(
    /**
     * The new group ID of the artifact.
     */
    val groupId: String? = null,
    /**
     * The new artifact ID of the artifact.
     */
    val artifactId: String? = null,
    /**
     * The new version of the artifact.
     */
    val version: String? = null,
    /**
     * The message to show the user for this relocation.
     */
    val message: String? = null,
) {

    fun applyTo(recipient: MavenPomRelocation) {
        relocation.groupId tryAssign groupId
        relocation.artifactId tryAssign artifactId
        relocation.version tryAssign version
        relocation.message tryAssign message
    }
}
