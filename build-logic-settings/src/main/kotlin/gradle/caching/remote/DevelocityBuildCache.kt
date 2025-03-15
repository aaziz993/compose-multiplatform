package gradle.caching.remote

import com.gradle.develocity.agent.gradle.buildcache.DevelocityBuildCache
import gradle.accessors.projectProperties
import gradle.accessors.resolveValue
import gradle.api.isCI
import gradle.caching.AbstractBuildCache
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.initialization.Settings
import org.gradle.caching.configuration.BuildCache
import org.gradle.caching.configuration.BuildCacheConfiguration
import org.gradle.kotlin.dsl.develocity

@Serializable
@SerialName("develocity")
internal data class DevelocityBuildCache(
    override val enabled: Boolean? = null,
    override val push: Boolean? = null,
    val server: String? = null,
    val path: String? = null,
    val allowUntrustedServer: Boolean? = null,
    val allowInsecureProtocol: Boolean? = null,
    val useExpectContinue: Boolean? = null,
    val usernameAndPassword: HttpBuildCacheCredentials? = null
) : AbstractBuildCache() {

    context(Settings)
    override fun applyTo(cache: BuildCache) {
        // better set it to true only for CI builds.
        cache.isPush = isCI && projectProperties.plugins.develocity.accessKey?.resolveValue() != null

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

    context(Settings)
    override fun applyTo(configuration: BuildCacheConfiguration) {
        configuration.remote(develocity.buildCache) {
            applyTo(this)
        }
    }
}
