package gradle.api.publish.maven

import gradle.api.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.publish.maven.MavenPomCiManagement

/**
 * The CI management system of a Maven publication.
 *
 * @since 4.8
 * @see MavenPom
 */
@Serializable
internal data class MavenPomCiManagement(
    /**
     * The name of this CI management system.
     */
    val system: String? = null,
    /**
     * The URL of this CI management system.
     */
    val url: String? = null,
) {

    fun applyTo(receiver: MavenPomCiManagement) {
        receiver.system tryAssign system
        receiver.url tryAssign url
    }
}
