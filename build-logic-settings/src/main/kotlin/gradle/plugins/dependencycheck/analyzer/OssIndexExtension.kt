package gradle.plugins.dependencycheck.analyzer

/**
 * The configuration for the OSS Index Analyzer.
 */
import kotlinx.serialization.Serializable
import org.owasp.dependencycheck.gradle.extension.OssIndexExtension

@Serializable
internal data class OssIndexExtension(
    /**
     * Sets whether the OSS Index Analyzer should be used.
     */
    val enabled: Boolean? = null,
    /**
     * The optional username to connect to the OSS Index
     */
    val username: String? = null,
    /**
     * The optional password or API token to connect to the OSS Index
     */
    val password: String? = null,
    /**
     * The OSS Index URL.
     */
    val url: String? = null,
    /**
     * Only output a warning message instead of failing when remote errors occur.
     */
    val warnOnlyOnRemoteErrors: Boolean? = null
) {

    fun applyTo(receiver: OssIndexExtension) {
        enabled?.let(receiver::setEnabled)
        username?.let(receiver::setUsername)
        password?.let(receiver::setPassword)
        url?.let(receiver::setUrl)
        warnOnlyOnRemoteErrors?.let(receiver::setWarnOnlyOnRemoteErrors)
    }
}
