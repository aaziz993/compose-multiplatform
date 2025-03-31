package gradle.api.publish.maven

import gradle.api.provider.tryAssign
import gradle.reflect.tryApply
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

    fun applyTo(receiver: MavenPomDistributionManagement) {
        receiver.downloadUrl tryAssign downloadUrl
        receiver::relocation tryApply relocation?.let { relocation -> relocation::applyTo }
    }
}
