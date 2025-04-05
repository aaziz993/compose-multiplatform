package gradle.caching.remote

import com.gradle.develocity.agent.gradle.buildcache.DevelocityBuildCache
import gradle.api.project.projectProperties
import gradle.api.ci.CI
import gradle.api.initialization.initializationProperties
import gradle.caching.AbstractBuildCache
import klib.data.type.reflection.trySet
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.initialization.Settings

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
    override fun applyTo(receiver: DevelocityBuildCache) {
        // better set it to true only for CI builds.
        receiver.isPush = CI.present && settings.initializationProperties.develocity?.accessKey != null

        super.applyTo(receiver)

        receiver::setServer trySet server
        receiver::setPath trySet path
        receiver::setAllowUntrustedServer trySet allowUntrustedServer
        receiver::setAllowInsecureProtocol trySet allowInsecureProtocol
        receiver::setUseExpectContinue trySet useExpectContinue
        usernameAndPassword?.let { (username, password) ->
            receiver.usernameAndPassword(username, password)
        }
    }
}
