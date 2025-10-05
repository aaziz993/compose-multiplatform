package klib.data.cryptography.pgp.model

import data.type.model.Compression

internal val COMPRESSION_MAP = mapOf(
    Compression.UNCOMPRESSED to 0,
    Compression.ZIP to 1,
    Compression.ZLIB to 2,
    Compression.BZIP2 to 3,
)
