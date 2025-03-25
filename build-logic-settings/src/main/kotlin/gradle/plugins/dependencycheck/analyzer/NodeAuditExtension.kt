package gradle.plugins.dependencycheck.analyzer

import groovy.transform.CompileStatic

/**
 * The configuration for the Node Audit Analyzer.
 */
import kotlinx.serialization.Serializable
import org.owasp.dependencycheck.gradle.extension.NodeAuditExtension

@Serializable
internal data class NodeAuditExtension(
    /**
     * Sets whether the Node Audit Analyzer should be used.
     */
    val enabled: Boolean? = null,
    /**
     * Sets whether the Node Audit Analyzer should cache results locally.
     */
    val useCache: Boolean? = null,
    /**
     * Sets whether the Node Audit Analyzer should skip devDependencies.
     */
    val skipDevDependencies: Boolean? = null,
    /**
     * Sets whether the Yarn Audit Analyzer should be used.
     */
    val yarnEnabled: Boolean? = null,
    /**
     * The path to `yarn`.
     */
    val yarnPath: String? = null,
    /**
     * Sets whether the Pnpm Audit Analyzer should be used.
     */
    val pnpmEnabled: Boolean? = null,
    /**
     * The path to `pnpm`.
     */
    val pnpmPath: String? = null,
    /**
     * The URL to the NPM Audit API.
     */
    val url: String? = null
) {

    fun applyTo(receiver: NodeAuditExtension) {
        enabled?.let(receiver::setEnabled)
        useCache?.let(receiver::setUseCache)
        skipDevDependencies?.let(receiver::setSkipDevDependencies)
        yarnEnabled?.let(receiver::setYarnEnabled)
        yarnPath?.let(receiver::setYarnPath)
        pnpmEnabled?.let(receiver::setPnpmEnabled)
        pnpmPath?.let(receiver::setPnpmPath)
        url?.let(receiver::setUrl)
    }
}
