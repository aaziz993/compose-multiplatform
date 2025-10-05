package klib.data.cryptography.pgp.model

import kotlinx.serialization.Serializable

@Serializable
internal data class ReadMessageOptions(
    var armoredMessage: String? = null,
    var binaryMessage: ByteArray? = null,
)
