package gradle.caching.remote

import gradle.api.isCI
import gradle.caching.AbstractBuildCache
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.initialization.Settings
import org.gradle.caching.configuration.BuildCache
import org.gradle.caching.configuration.BuildCacheConfiguration
import org.gradle.caching.http.HttpBuildCache

@Serializable
@SerialName("http")
internal data class HttpBuildCache(
    override val isEnabled: Boolean? = null,
    override val isPush: Boolean? = null,
    val url: String,
    val credentials: HttpBuildCacheCredentials? = null,
    val allowUntrustedServer: Boolean? = null,
    val allowInsecureProtocol: Boolean? = null,
    val useExpectContinue: Boolean? = null,
) : AbstractBuildCache() {

    context(Settings)
    override fun applyTo(cache: BuildCache) {
        // better set it to true only for CI builds.
        cache.isPush = isCI

        super.applyTo(cache)

        cache as HttpBuildCache

        cache.setUrl(url)
        credentials?.applyTo(cache.credentials)
        allowUntrustedServer?.let(cache::setAllowUntrustedServer)
        allowInsecureProtocol?.let(cache::setAllowInsecureProtocol)
        useExpectContinue?.let(cache::setUseExpectContinue)
    }

    context(Settings)
    override fun applyTo(configuration: BuildCacheConfiguration) {
        configuration.remote(HttpBuildCache::class.java) {
            applyTo(this)
        }
    }
}
