package gradle.caching.remote

import gradle.plugins.credentials.PasswordCredentials
import kotlinx.serialization.Serializable

@Serializable
internal data class HttpBuildCacheCredentials(
    override val username: String,
    override val password: String
) : PasswordCredentials
