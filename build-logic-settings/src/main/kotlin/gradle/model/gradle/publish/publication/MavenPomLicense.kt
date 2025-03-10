package gradle.model.gradle.publish.publication

import gradle.tryAssign
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

    fun applyTo(license: MavenPomLicense) {
        license.name tryAssign name
        license.url tryAssign url
        license.distribution tryAssign distribution
        license.comments tryAssign comments
    }
}
