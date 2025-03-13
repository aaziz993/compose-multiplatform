package gradle.caching

import gradle.plugins.credentials.PasswordCredentials
import kotlinx.serialization.Serializable

@Serializable
internal data class HttpBuildCacheCredentials(
    override val username: String? = null,
    override val password: String? = null
) : PasswordCredentials
