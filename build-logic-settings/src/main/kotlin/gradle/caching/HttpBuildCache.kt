package gradle.caching

import kotlinx.serialization.Serializable
import org.gradle.caching.configuration.BuildCache
import org.gradle.caching.configuration.BuildCacheConfiguration
import org.gradle.caching.http.HttpBuildCache

@Serializable
internal data class HttpBuildCache(
    override val isEnabled: Boolean? = null,
    override val isPush: Boolean? = null,
    val url: String,
    val credentials: HttpBuildCacheCredentials? = null,
    val allowUntrustedServer: Boolean? = null,
    val allowInsecureProtocol: Boolean? = null,
    val useExpectContinue: Boolean? = null,
) : AbstractBuildCache() {

    override val type: Class<out BuildCache>
        get() = HttpBuildCache::class.java

    override fun applyTo(cache: BuildCache) {
        super.applyTo(cache)

        cache as HttpBuildCache

        cache.setUrl(url)
        credentials?.applyTo(cache.credentials)
        allowUntrustedServer?.let(cache::setAllowUntrustedServer)
        allowInsecureProtocol?.let(cache::setAllowInsecureProtocol)
        useExpectContinue?.let(cache::setUseExpectContinue)
    }

    override fun applyTo(configuration: BuildCacheConfiguration) {
        super.applyTo(configuration)
    }
}
