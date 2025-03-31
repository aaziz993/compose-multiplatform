package gradle.plugins.dependencycheck.analyzer

/**
 * The configuration for the Node Audit Analyzer.
 */
import gradle.reflect.trySet
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
        receiver::setEnabled trySet enabled
        receiver::setUseCache trySet useCache
        receiver::setSkipDevDependencies trySet skipDevDependencies
        receiver::setYarnEnabled trySet yarnEnabled
        receiver::setYarnPath trySet yarnPath
        receiver::setPnpmEnabled trySet pnpmEnabled
        receiver::setPnpmPath trySet pnpmPath
        receiver::setUrl trySet url
    }
}
