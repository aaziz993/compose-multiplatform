package klib.data.cryptography.pgp.model

import klib.data.cryptography.model.SymmetricAlgorithm

internal val SYMMETRIC_ALGORITHMS = mapOf(
    SymmetricAlgorithm.PLAINTEXT to 0,
    SymmetricAlgorithm.TRIPLEDES to 2,
    SymmetricAlgorithm.CAST_5 to 3,
    SymmetricAlgorithm.BLOWFISH to 4,
    SymmetricAlgorithm.AES_128 to 7,
    SymmetricAlgorithm.AES_192 to 8,
    SymmetricAlgorithm.AES_256 to 9,
    SymmetricAlgorithm.TWOFISH to 10,
)
