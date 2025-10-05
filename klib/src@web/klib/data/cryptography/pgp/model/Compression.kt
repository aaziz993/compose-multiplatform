package klib.data.cryptography.pgp.model

import klib.data.model.Compression

internal val COMPRESSIONS = mapOf(
    Compression.UNCOMPRESSED to 0,
    Compression.ZIP to 1,
    Compression.ZLIB to 2,
    Compression.BZIP2 to 3,
)
