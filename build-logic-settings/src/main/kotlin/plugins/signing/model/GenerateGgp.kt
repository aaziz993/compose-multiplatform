package plugins.signing.model

import kotlinx.serialization.Serializable

@Serializable
internal data class GenerateGgp(
    val keyType: String = "RSA",
    val keyLength: String = "4096",
    val subkeyType: String = " RSA",
    val subkeyLength: String = "4096",
    val nameReal: String,
    val nameComment: String = "",
    val nameEmail: String,
    val expireDate: Long = 0,
    val passphrase: String,
)
