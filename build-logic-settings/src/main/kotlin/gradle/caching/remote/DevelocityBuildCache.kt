package gradle.caching.remote

import com.gradle.develocity.agent.gradle.buildcache.DevelocityBuildCache
import gradle.caching.AbstractBuildCache
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.caching.configuration.BuildCache

@Serializable
@SerialName("develocity")
internal data class DevelocityBuildCache(
    override val isEnabled: Boolean? = null,
    override val isPush: Boolean? = null,
    val server: String? = null,
    val path: String? = null,
    val allowUntrustedServer: Boolean? = null,
    val allowInsecureProtocol: Boolean? = null,
    val useExpectContinue: Boolean? = null,
    val usernameAndPassword: HttpBuildCacheCredentials? = null
) : AbstractBuildCache() {

    override val type: Class<out BuildCache>
        get() = DevelocityBuildCache::class.java

    override fun applyTo(cache: BuildCache) {
        super.applyTo(cache)

        cache as DevelocityBuildCache

        server?.let(cache::setServer)
        path?.let(cache::setPath)
        allowUntrustedServer?.let(cache::setAllowUntrustedServer)
        allowInsecureProtocol?.let(cache::setAllowInsecureProtocol)
        useExpectContinue?.let(cache::setUseExpectContinue)
        usernameAndPassword?.let { (username, password) ->
            cache.usernameAndPassword(username, password)
        }
    }
}
