package gradle.plugins.publish

import gradle.api.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.publish.maven.MavenPomScm

/**
 * The SCM (source control management) of a Maven publication.
 *
 * @since 4.8
 * @see MavenPom
 */
@Serializable
internal data class MavenPomScm(
    /**
     * The connection URL of this SCM.
     */
    val connection: String? = null,
    /**
     * The developer connection URL of this SCM.
     */
    val developerConnection: String? = null,
    /**
     * The browsable repository URL of this SCM.
     */
    val url: String? = null,
    /**
     * The tag of current code in this SCM.
     */
    val tag: String? = null,
) {

    fun applyTo(scm: MavenPomScm) {
        scm.connection tryAssign connection
        scm.developerConnection tryAssign developerConnection
        scm.url tryAssign url
        scm.tag tryAssign tag
    }
}
