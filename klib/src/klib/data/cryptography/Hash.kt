package klib.data.cryptography

import klib.data.cryptography.model.HashAlgorithm
import dev.whyoleg.cryptography.CryptographyAlgorithmId
import dev.whyoleg.cryptography.CryptographyProvider
import dev.whyoleg.cryptography.algorithms.Digest
import dev.whyoleg.cryptography.algorithms.MD5 as CRYPTO_MD5
import dev.whyoleg.cryptography.algorithms.SHA1 as CRYPTO_SHA1
import dev.whyoleg.cryptography.algorithms.SHA224 as CRYPTO_SHA224
import dev.whyoleg.cryptography.algorithms.SHA256 as CRYPTO_SHA256
import dev.whyoleg.cryptography.algorithms.SHA384 as CRYPTO_SHA384
import dev.whyoleg.cryptography.algorithms.SHA3_224 as CRYPTO_SHA3_224
import dev.whyoleg.cryptography.algorithms.SHA3_256 as CRYPTO_SHA3_256
import dev.whyoleg.cryptography.algorithms.SHA3_384 as CRYPTO_SHA3_384
import dev.whyoleg.cryptography.algorithms.SHA3_512 as CRYPTO_SHA3_512
import dev.whyoleg.cryptography.algorithms.SHA512 as CRYPTO_SHA512
import dev.whyoleg.cryptography.operations.Hasher

private fun hasher(identifier: CryptographyAlgorithmId<Digest>): Hasher =
    CryptographyProvider.Default
        .get(identifier)
        .hasher()

public fun hasher(algorithm: HashAlgorithm): Hasher = hasher(
    when (algorithm) {
        HashAlgorithm.MD5 -> CRYPTO_MD5

        HashAlgorithm.SHA1 -> CRYPTO_SHA1

        HashAlgorithm.SHA224 -> CRYPTO_SHA224

        HashAlgorithm.SHA256 -> CRYPTO_SHA256

        HashAlgorithm.SHA384 -> CRYPTO_SHA384

        HashAlgorithm.SHA512 -> CRYPTO_SHA512

        HashAlgorithm.SHA3_224 -> CRYPTO_SHA3_224

        HashAlgorithm.SHA3_256 -> CRYPTO_SHA3_256

        HashAlgorithm.SHA3_384 -> CRYPTO_SHA3_384

        HashAlgorithm.SHA3_512 -> CRYPTO_SHA3_512

        else -> throw IllegalArgumentException("Unknown hash algorithm \"$algorithm\"")
    },
)

public val MD5: Hasher by lazy { hasher(CRYPTO_MD5) }

public val SHA1: Hasher by lazy { hasher(CRYPTO_SHA1) }

public val SHA224: Hasher by lazy { hasher(CRYPTO_SHA224) }

public val SHA256: Hasher by lazy { hasher(CRYPTO_SHA256) }

public val SHA384: Hasher by lazy { hasher(CRYPTO_SHA384) }

public val SHA512: Hasher by lazy { hasher(CRYPTO_SHA512) }

public val SHA3_224: Hasher by lazy { hasher(CRYPTO_SHA3_224) }

public val SHA3_256: Hasher by lazy { hasher(CRYPTO_SHA3_256) }

public val SHA3_384: Hasher by lazy { hasher(CRYPTO_SHA3_384) }

public val SHA3_512: Hasher by lazy { hasher(CRYPTO_SHA3_512) }

public suspend fun ByteArray.hashMd5(): ByteArray = MD5.hash(this)

public fun ByteArray.hashMd5Blocking(): ByteArray = MD5.hashBlocking(this)

public suspend fun ByteArray.hashSha1(): ByteArray = SHA1.hash(this)

public fun ByteArray.hashSha1Blocking(): ByteArray = SHA1.hashBlocking(this)

public suspend fun ByteArray.hashSha224(): ByteArray = SHA224.hash(this)

public fun ByteArray.hashSha224Blocking(): ByteArray = SHA224.hashBlocking(this)

public suspend fun ByteArray.hashSha256(): ByteArray = SHA256.hash(this)

public fun ByteArray.hashSha256Blocking(): ByteArray = SHA256.hashBlocking(this)

public suspend fun ByteArray.hashSha384(): ByteArray = SHA384.hash(this)

public fun ByteArray.hashSha384Blocking(): ByteArray = SHA384.hashBlocking(this)

public suspend fun ByteArray.hashSha512(): ByteArray = SHA512.hash(this)

public fun ByteArray.hashSha512Blocking(): ByteArray = SHA512.hashBlocking(this)

public suspend fun ByteArray.hashSha3224(): ByteArray = SHA3_224.hash(this)

public fun ByteArray.hashSha3224Blocking(): ByteArray = SHA3_224.hashBlocking(this)

public suspend fun ByteArray.hashSha3256(): ByteArray = SHA3_256.hash(this)

public fun ByteArray.hashSha3256Blocking(): ByteArray = SHA3_256.hashBlocking(this)

public suspend fun ByteArray.hashSha3384(): ByteArray = SHA3_384.hash(this)

public fun ByteArray.hashSha3384Blocking(): ByteArray = SHA3_384.hashBlocking(this)

public suspend fun ByteArray.hashSha3512(): ByteArray = SHA3_512.hash(this)

public fun ByteArray.hashSha3512Blocking(): ByteArray = SHA3_512.hashBlocking(this)
