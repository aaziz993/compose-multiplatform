package gradle.plugins.dependencycheck.analyzer

/**
 * The configuration for the Node Package Analyzer.
 */
import gradle.api.trySet
import kotlinx.serialization.Serializable
import org.owasp.dependencycheck.gradle.extension.NodePackageExtension

@Serializable
internal data class NodePackageExtension(
    /**
     * Sets whether the Node Package Analyzer should be used.
     */
    val enabled: Boolean? = null,
    /**
     * Sets whether the Node Package Analyzer should skip devDependencies.
     */
    val skipDevDependencies: Boolean? = null
) {

    fun applyTo(receiver: NodePackageExtension) {
        receiver::setEnabled trySet enabled
        receiver::setSkipDevDependencies trySet skipDevDependencies
    }
}
