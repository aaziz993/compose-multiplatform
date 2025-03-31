package gradle.plugins.dependencycheck

import gradle.reflect.trySet
import kotlinx.serialization.Serializable
import org.owasp.dependencycheck.gradle.extension.CacheExtension

/**
 * The configuration for caching external results.
 */
@Serializable
internal data class CacheExtension(
    /**
     * Sets whether the OSS Index Analyzer's results should be cached locally.
     * Cache expires after 24 hours.
     */
    val ossIndex: Boolean? = null,
    /**
     * Sets whether the Central Analyzer's results should be cached locally.
     * Cache expires after 30 days.
     */
    val central: Boolean? = null,
    /**
     * Sets whether the Node Audit Analyzer's results should be cached locally.
     * Cache expires after 24 hours.
     */
    val nodeAudit: Boolean? = null,
) {

    fun applyTo(receiver: CacheExtension) {
        receiver::setOssIndex trySet ossIndex
        receiver::setCentral trySet central
        receiver::setNodeAudit trySet nodeAudit
    }
}
