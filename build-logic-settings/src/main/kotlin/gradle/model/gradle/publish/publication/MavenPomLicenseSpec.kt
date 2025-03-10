package gradle.model.gradle.publish.publication

import kotlinx.serialization.Serializable
import org.gradle.api.publish.maven.MavenPomLicenseSpec

/**
 * Allows to add licenses to a Maven publication.
 *
 * @since 4.8
 * @see MavenPom
 *
 * @see MavenPomLicense
 */
@Serializable
internal data class MavenPomLicenseSpec(
    /**
     * Creates, configures and adds a license to the publication.
     */
    val license: MavenPomLicense? = null,
) {

    fun applyTo(spec: MavenPomLicenseSpec) {
        license?.let { license ->
            spec.license(license::applyTo)
        }
    }
}
