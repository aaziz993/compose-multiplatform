package gradle.api.publish.maven

import gradle.api.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.publish.maven.MavenPomLicense

/**
 * A license of a Maven publication.
 *
 * @since 4.8
 * @see MavenPom
 *
 * @see MavenPomLicenseSpec
 */
@Serializable
internal data class MavenPomLicense(
    /**
     * The name of this license.
     */
    val name: String? = null,
    /**
     * The URL of this license.
     */
    val url: String? = null,
    /**
     * The distribution of this license.
     */
    val distribution: String? = null,
    /**
     * The comments of this license.
     */
    val comments: String? = null,
) {

    fun applyTo(receiver: MavenPomLicense) {
        receiver.name tryAssign name
        receiver.url tryAssign url
        receiver.distribution tryAssign distribution
        receiver.comments tryAssign comments
    }
}
