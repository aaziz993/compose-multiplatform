package gradle.plugins.dependencycheck.analyzer

/**
 * The artifactory analyzer configuration.
 */
import klib.data.type.reflection.trySet
import kotlinx.serialization.Serializable
import org.owasp.dependencycheck.gradle.extension.ArtifactoryExtension

@Serializable
internal data class ArtifactoryExtension(
    /**
     * Sets whether the Artifactory Analyzer should be used.
     */
    val enabled: Boolean? = null,
    /**
     * The Artifactory server URL.
     */
    val url: String? = null,
    /**
     * Whether Artifactory should be accessed through a proxy or not.
     */
    val usesProxy: Boolean? = null,
    /**
     * Whether the Artifactory analyzer should be run in parallel or not.
     */
    val parallelAnalysis: Boolean? = null,
    /**
     * The user name (only used with API token) to connect to Artifactory instance.
     */
    val username: String? = null,
    /**
     * The API token to connect to Artifactory instance.
     */
    val apiToken: String? = null,
    /**
     * The bearer token to connect to Artifactory instance.
     */
    val bearerToken: String? = null
) {

    fun applyTo(receiver: ArtifactoryExtension) {
        receiver::setEnabled trySet enabled
        receiver::setUrl trySet url
        receiver::setUsesProxy trySet usesProxy
        receiver::setParallelAnalysis trySet parallelAnalysis
        receiver::setUsername trySet username
        receiver::setApiToken trySet apiToken
        receiver::setBearerToken trySet bearerToken
    }
}
