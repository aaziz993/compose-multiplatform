package gradle.plugins.dependencycheck.analyzer

import groovy.transform.CompileStatic

/**
 * The artifactory analyzer configuration.
 */
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
        enabled?.let(receiver::setEnabled)
        url?.let(receiver::setUrl)
        usesProxy?.let(receiver::setUsesProxy)
        parallelAnalysis?.let(receiver::setParallelAnalysis)
        username?.let(receiver::setUsername)
        apiToken?.let(receiver::setApiToken)
        bearerToken?.let(receiver::setBearerToken)
    }
}
