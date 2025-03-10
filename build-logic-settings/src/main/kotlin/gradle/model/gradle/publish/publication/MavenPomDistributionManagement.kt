package gradle.model.gradle.publish.publication

import gradle.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.publish.maven.MavenPomDistributionManagement

/**
 * The distribution management configuration of a Maven publication.
 *
 * @since 4.8
 * @see MavenPom
 */
@Serializable
internal data class MavenPomDistributionManagement(
    /**
     * The download URL of the corresponding Maven publication.
     */
    val downloadUrl: String? = null,
    /**
     * Configures the relocation information.
     */
    val relocation: MavenPomRelocation? = null,
) {

    fun applyTo(management: MavenPomDistributionManagement) {
        management.downloadUrl tryAssign downloadUrl
        relocation?.let { relocation ->
            management.relocation(relocation::applyTo)
        }
    }
}
