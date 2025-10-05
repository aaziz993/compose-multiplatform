package klib.data.cryptography.pgp.model

import kotlinx.serialization.Serializable

@Serializable
internal data class CreateMessageOptions(
    var text: String? = null,
    var binary: ByteArray? = null,
)
