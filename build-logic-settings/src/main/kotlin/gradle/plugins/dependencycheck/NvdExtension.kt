package gradle.plugins.dependencycheck

import kotlinx.serialization.Serializable
import org.owasp.dependencycheck.gradle.extension.NvdExtension

@Serializable
internal data class NvdExtension(
    /**
     * The API Key to access the NVD API; obtained from https://nvd.nist.gov/developers/request-an-api-key.
     */
    val apiKey: String? = null,
    /**
     * The number of milliseconds to wait between calls to the NVD API.
     */
    val delay: Int? = null,
    /**
     * The number records for a single page from NVD API (must be <=2000).
     */
    val resultsPerPage: Int? = null,
    /**
     * The maximum number of retry requests for a single call to the NVD API.
     */
    val maxRetryCount: Int? = null,
    /**
     * The URL for the NVD API Data feed that can be generated using https://github.com/jeremylong/Open-Vulnerability-Project/tree/main/vulnz#caching-the-nvd-cve-data.
     */
    val datafeedUrl: String? = null,
    /**
     * Credentials used for basic authentication for the NVD API Data feed.
     */
    val datafeedUser: String? = null,
    /**
     * Credentials used for basic authentication for the NVD API Data feed.
     */
    val datafeedPassword: String? = null,
    /**
     * Credentials used for bearer authentication for the NVD API Data feed.
     */
    val datafeedBearerToken: String? = null,
    /**
     * The number of hours to wait before checking for new updates from the NVD. The default is 4 hours.
     */
    val validForHours: Int? = null,
    /**
     * The NVD API endpoint URL; configuring this is uncommon.
     */
    val endpoint: String? = null,
) {

    fun applyTo(receiver: NvdExtension) {
        apiKey?.let(receiver::setApiKey)
        delay?.let(receiver::setDelay)
        resultsPerPage?.let(receiver::setResultsPerPage)
        maxRetryCount?.let(receiver::setMaxRetryCount)
        datafeedUrl?.let(receiver::setDatafeedUrl)
        datafeedUser?.let(receiver::setDatafeedUser)
        datafeedPassword?.let(receiver::setDatafeedPassword)
        datafeedBearerToken?.let(receiver::setDatafeedBearerToken)
        validForHours?.let(receiver::setValidForHours)
        endpoint?.let(receiver::setEndpoint)
    }
}
