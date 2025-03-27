package gradle.caching.remote

import gradle.caching.AbstractBuildCache
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.initialization.Settings
import org.gradle.caching.http.HttpBuildCache

@Serializable
@SerialName("http")
internal data class HttpBuildCache(
    override val enabled: Boolean? = null,
    override val push: Boolean? = null,
    val url: String,
    val credentials: HttpBuildCacheCredentials? = null,
    val allowUntrustedServer: Boolean? = null,
    val allowInsecureProtocol: Boolean? = null,
    val useExpectContinue: Boolean? = null,
) : AbstractBuildCache<HttpBuildCache>() {

    context(Settings)
    override fun applyTo(receiver: HttpBuildCache) {
        super.applyTo(receiver)

        receiver.setUrl(url)
        credentials?.applyTo(receiver.credentials)
        allowUntrustedServer?.let(receiver::setAllowUntrustedServer)
        allowInsecureProtocol?.let(receiver::setAllowInsecureProtocol)
        useExpectContinue?.let(receiver::setUseExpectContinue)
    }
}
