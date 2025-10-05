package klib.data.cryptography.pgp.model

public data class PGPSubKeyType(
    public val key: PGPKey? = null,
    public val sign: Boolean = false,
)
