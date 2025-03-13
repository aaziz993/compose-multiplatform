package gradle.plugins.signing

import kotlinx.serialization.Serializable

@Serializable
internal data class InMemoryPgpKeys(
    val defaultKeyId: String? = null,
    val defaultSecretKey: String? = null,
    val defaultPassword: String? = null
)
