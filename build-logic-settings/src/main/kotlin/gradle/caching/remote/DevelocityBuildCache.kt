package gradle.caching.remote

import com.gradle.develocity.agent.gradle.buildcache.DevelocityBuildCache
import gradle.accessors.projectProperties
import gradle.api.isCI
import gradle.caching.AbstractBuildCache
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.initialization.Settings
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
) : AbstractBuildCache<DevelocityBuildCache>() {

    context(settings: Settings)
    override fun applyTo(receiver: DevelocityBuildCache) {
        // better set it to true only for CI builds.
        receiver.isPush = isCI && projectProperties.plugins.develocity.accessKey != null

        super.applyTo(receiver)

        server?.let(receiver::setServer)
        path?.let(receiver::setPath)
        allowUntrustedServer?.let(receiver::setAllowUntrustedServer)
        allowInsecureProtocol?.let(receiver::setAllowInsecureProtocol)
        useExpectContinue?.let(receiver::setUseExpectContinue)
        usernameAndPassword?.let { (username, password) ->
            receiver.usernameAndPassword(username, password)
        }
    }

    context(settings: Settings)
    override fun applyTo(receiver: BuildCacheConfiguration) {
        receiver.remote(develocity.buildCache) {
            applyTo(this)
        }
    }
}
