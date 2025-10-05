package klib.data.cryptography.pgp.model

internal data class ReadSignatureOptions(
    var armoredSignature: String? = null,
    var binarySignature: ByteArray? = null,
)
