package klib.data.cryptography.pgp.model

import klib.data.cryptography.model.SymmetricAlgorithm
import org.pgpainless.algorithm.SymmetricKeyAlgorithm

internal val SYMMETRIC_ALGORITHMS = mapOf(
    SymmetricAlgorithm.PLAINTEXT to SymmetricKeyAlgorithm.NULL,
    SymmetricAlgorithm.TRIPLEDES to SymmetricKeyAlgorithm.TRIPLE_DES,
    SymmetricAlgorithm.CAST_5 to SymmetricKeyAlgorithm.CAST5,
    SymmetricAlgorithm.BLOWFISH to SymmetricKeyAlgorithm.BLOWFISH,
    SymmetricAlgorithm.AES_128 to SymmetricKeyAlgorithm.AES_128,
    SymmetricAlgorithm.AES_192 to SymmetricKeyAlgorithm.AES_192,
    SymmetricAlgorithm.AES_256 to SymmetricKeyAlgorithm.AES_256,
    SymmetricAlgorithm.TWOFISH to SymmetricKeyAlgorithm.TWOFISH,
)
