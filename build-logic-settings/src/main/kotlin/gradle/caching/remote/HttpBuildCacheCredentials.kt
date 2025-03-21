package gradle.caching.remote

import gradle.plugins.credentials.PasswordCredentials
import kotlinx.serialization.Serializable
import org.gradle.caching.http.HttpBuildCacheCredentials

@Serializable
internal data class HttpBuildCacheCredentials(
    override val username: String? = null,
    override val password: String? = null,
) : PasswordCredentials<HttpBuildCacheCredentials>
