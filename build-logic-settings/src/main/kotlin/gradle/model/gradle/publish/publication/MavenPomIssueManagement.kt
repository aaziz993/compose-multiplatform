package gradle.model.gradle.publish.publication

import gradle.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.publish.maven.MavenPomIssueManagement

/**
 * The issue management system of a Maven publication.
 *
 * @since 4.8
 * @see MavenPom
 */
@Serializable
internal data class MavenPomIssueManagement(
    /**
     * The name of this issue management system.
     */
    val system: String? = null,
    /**
     * The URL of this issue management system.
     */
    val url: String? = null,
) {

    fun applyTo(management: MavenPomIssueManagement) {
        management.system tryAssign system
        management.url tryAssign url
    }
}
