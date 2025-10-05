package klib.data.cryptography.pgp.model

import klib.data.cryptography.model.HashAlgorithm

internal val HASH_ALGORITHMS = mapOf(
    HashAlgorithm.MD5 to 1,
    HashAlgorithm.SHA1 to 2,
    HashAlgorithm.RIPEMD160 to 3,
    HashAlgorithm.SHA256 to 8,
    HashAlgorithm.SHA384 to 9,
    HashAlgorithm.SHA512 to 10,
    HashAlgorithm.SHA224 to 11,
)
