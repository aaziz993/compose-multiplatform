package gradle.api.publish.maven

import kotlinx.serialization.Serializable

/**
 * A license of a Maven publication.
 *
 * @since 4.8
 * @see MavenPom
 *
 * @see MavenPomLicenseSpec
 */
@Serializable
public data class MavenPomLicense(
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
)
