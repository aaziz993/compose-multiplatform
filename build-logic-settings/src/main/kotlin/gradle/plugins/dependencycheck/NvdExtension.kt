package gradle.plugins.dependencycheck

import klib.data.type.reflection.trySet
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
        receiver::setApiKey trySet apiKey
        receiver::setDelay trySet delay
        receiver::setResultsPerPage trySet resultsPerPage
        receiver::setMaxRetryCount trySet maxRetryCount
        receiver::setDatafeedUrl trySet datafeedUrl
        receiver::setDatafeedUser trySet datafeedUser
        receiver::setDatafeedPassword trySet datafeedPassword
        receiver::setDatafeedBearerToken trySet datafeedBearerToken
        receiver::setValidForHours trySet validForHours
        receiver::setEndpoint trySet endpoint
    }
}
