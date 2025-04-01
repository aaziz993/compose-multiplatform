package gradle.plugins.dependencycheck.analyzer

import klib.data.type.reflection.trySet
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
        receiver::setEnabled trySet enabled
        receiver::setUrl trySet url
        receiver::setUser trySet user
        receiver::setPassword trySet password
        receiver::setBearerToken trySet bearerToken
        receiver::setValidForHours trySet validForHours
    }
}
