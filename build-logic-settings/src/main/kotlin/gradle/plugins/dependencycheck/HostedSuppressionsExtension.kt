package gradle.plugins.dependencycheck

import kotlinx.serialization.Serializable
import org.owasp.dependencycheck.gradle.extension.HostedSuppressionsExtension

/**
 * The advanced configuration for the hosted suppressions file.
 */
@Serializable
internal data class HostedSuppressionsExtension(
    /**
     * Whether the hosted suppressions fie will be used.
     */
    val enabled: Boolean? = null,
    /**
     * The URL for a mirrored hosted suppressions file.
     */
    val url: String? = null,
    /**
     * Credentials used for basic authentication for a mirrored hosted suppressions file.
     */
    val user: String? = null,
    /**
     * Credentials used for basic authentication for a mirrored hosted suppressions file.
     */
    val password: String? = null,
    /**
     * Credentials used for bearer authentication for a mirrored hosted suppressions file.
     */
    val bearerToken: String? = null,
    /**
     * Whether the hosted suppressions file should be updated regardless of the `autoupdate` setting.
     */
    val forceupdate: Boolean? = null,
    /**
     * The number of hours to wait before checking for changes in the hosted suppressions file.
     */
    val validForHours: Int? = null,
) {

    fun applyTo(receiver: HostedSuppressionsExtension) {
        enabled?.let(receiver::setEnabled)
        url?.let(receiver::setUrl)
        user?.let(receiver::setUser)
        password?.let(receiver::setPassword)
        bearerToken?.let(receiver::setBearerToken)
        forceupdate?.let(receiver::setForceupdate)
        validForHours?.let(receiver::setValidForHours)
    }
}
