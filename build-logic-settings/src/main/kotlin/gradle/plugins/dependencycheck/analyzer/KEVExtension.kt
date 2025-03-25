package gradle.plugins.dependencycheck.analyzer

import kotlinx.serialization.Serializable
import org.owasp.dependencycheck.gradle.extension.KEVExtension

@Serializable
internal data class KEVExtension(
    /**
     * Sets whether the Known Exploited Vulnerability update and Analyzer will be used.
     */
    val enabled: Boolean? = null,
    /**
     * URL to the CISA Known Exploited Vulnerabilities JSON data feed.
     */
    val url: String? = null,
    /**
     * Credentials used for basic authentication for the CISA Known Exploited Vulnerabilities JSON data feed.
     */
    val user: String? = null,
    /**
     * Credentials used for basic authentication for the CISA Known Exploited Vulnerabilities JSON data feed.
     */
    val password: String? = null,
    /**
     * Credentials used for bearer authentication for the CISA Known Exploited Vulnerabilities JSON data feed.
     */
    val bearerToken: String? = null,
    /**
     * Controls the skipping of the check for Known Exploited Vulnerabilities updates.
     */
    val validForHours: Int? = null
) {

    fun applyTo(receiver: KEVExtension) {
        enabled?.let(receiver::setEnabled)
        url?.let(receiver::setUrl)
        user?.let(receiver::setUser)
        password?.let(receiver::setPassword)
        bearerToken?.let(receiver::setBearerToken)
        validForHours?.let(receiver::setValidForHours)
    }
}
