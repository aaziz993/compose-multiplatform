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

    context(Settings)
    override fun applyTo(recipient: DevelocityBuildCache) {
        // better set it to true only for CI builds.
        recipient.isPush = isCI && projectProperties.plugins.develocity.accessKey != null

        super.applyTo(recipient)

        server?.let(recipient::setServer)
        path?.let(recipient::setPath)
        allowUntrustedServer?.let(recipient::setAllowUntrustedServer)
        allowInsecureProtocol?.let(recipient::setAllowInsecureProtocol)
        useExpectContinue?.let(recipient::setUseExpectContinue)
        usernameAndPassword?.let { (username, password) ->
            recipient.usernameAndPassword(username, password)
        }
    }

    context(Settings)
    override fun applyTo(recipient: BuildCacheConfiguration) {
        recipient.remote(develocity.buildCache) {
            applyTo(this)
        }
    }
}
