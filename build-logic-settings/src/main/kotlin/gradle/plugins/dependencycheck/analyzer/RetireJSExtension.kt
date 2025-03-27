package gradle.plugins.dependencycheck.analyzer

/**
 * The configuration for the RetireJS Analyzer.
 */
import gradle.api.trySet
import kotlinx.serialization.Serializable
import org.owasp.dependencycheck.gradle.extension.RetireJSExtension

@Serializable
internal data class RetireJSExtension(
    /**
     * Sets whether the RetireJS Analyzer should be used.
     */
    val enabled: Boolean? = null,
    /**
     * The JS content filters (regular expressions) used to filter which JS files will be skipped if the content matches one
     * of the filters. This is most commonly used to filter by copyright.
     */
    val filters: List<String?>? = null,
    /**
     * Whether the Retire JS analyzer should filter the non-vunerable JS from the report.
     */
    val filterNonVulnerable: Boolean? = null,
    /**
     * The Retire JS Repository URL.
     */
    val retireJsUrl: String? = null,
    /**
     * Credentials used for basic authentication for the Retire JS Repository URL.
     */
    val user: String? = null,
    /**
     * Credentials used for basic authentication for the Retire JS Repository URL.
     */
    val password: String? = null,
    /**
     * Credentials used for bearer authentication for the Retire JS Repository URL.
     */
    val bearerToken: String? = null,
    /**
     * Whether the Retire JS analyzer should be updated regardless of the `autoupdate` setting.
     */
    val forceupdate: Boolean? = null
) {

    fun applyTo(receiver: RetireJSExtension) {
        receiver::setEnabled trySet enabled
        receiver::setFilters trySet filters
        receiver::setFilterNonVulnerable trySet filterNonVulnerable
        receiver::setRetireJsUrl trySet retireJsUrl
        receiver::setUser trySet user
        receiver::setPassword trySet password
        receiver::setBearerToken trySet bearerToken
        receiver::setForceupdate trySet forceupdate
    }
}
