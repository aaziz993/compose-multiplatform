package klib.data.cryptography.pgp.model

import klib.data.cryptography.model.HashAlgorithm
import org.pgpainless.algorithm.HashAlgorithm as PGPHashAlgorithm

internal val HASH_ALGORITHMS = mapOf(
    HashAlgorithm.MD5 to PGPHashAlgorithm.MD5,
    HashAlgorithm.SHA1 to PGPHashAlgorithm.SHA1,
    HashAlgorithm.RIPEMD160 to PGPHashAlgorithm.RIPEMD160,
    HashAlgorithm.SHA224 to PGPHashAlgorithm.SHA224,
    HashAlgorithm.SHA256 to PGPHashAlgorithm.SHA224,
    HashAlgorithm.SHA384 to PGPHashAlgorithm.SHA384,
    HashAlgorithm.SHA512 to PGPHashAlgorithm.SHA512,
)
