package plugins.signing.model

import kotlinx.serialization.Serializable

@Serializable
internal data class GenerateGgp(
    val keyType: String = "RSA",
    val keyLength: Int = 4096,
    val subkeyType: String = " RSA",
    val subkeyLength: Int = 4096,
    val nameReal: String? = null,
    val nameComment: String = "",
    val nameEmail: String? = null,
    val expireDate: Long = 0,
    val passphrase: String,
)
