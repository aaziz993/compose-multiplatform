package klib.data.cryptography

import klib.data.cryptography.model.HashAlgorithm
import dev.whyoleg.cryptography.CryptographyAlgorithmId
import dev.whyoleg.cryptography.CryptographyProvider
import dev.whyoleg.cryptography.algorithms.Digest
import dev.whyoleg.cryptography.algorithms.MD5
import dev.whyoleg.cryptography.algorithms.SHA1
import dev.whyoleg.cryptography.algorithms.SHA224
import dev.whyoleg.cryptography.algorithms.SHA256
import dev.whyoleg.cryptography.algorithms.SHA384
import dev.whyoleg.cryptography.algorithms.SHA3_224
import dev.whyoleg.cryptography.algorithms.SHA3_256
import dev.whyoleg.cryptography.algorithms.SHA3_384
import dev.whyoleg.cryptography.algorithms.SHA3_512
import dev.whyoleg.cryptography.algorithms.SHA512
import dev.whyoleg.cryptography.operations.Hasher

private fun hasher(identifier: CryptographyAlgorithmId<Digest>): Hasher =
    CryptographyProvider.Default
        .get(identifier)
        .hasher()

public fun hasher(algorithm: HashAlgorithm): Hasher = hasher(
    when (algorithm) {
        HashAlgorithm.MD5 -> MD5

        HashAlgorithm.SHA1 -> SHA1

        HashAlgorithm.SHA224 -> SHA224

        HashAlgorithm.SHA256 -> SHA256

        HashAlgorithm.SHA384 -> SHA384

        HashAlgorithm.SHA512 -> SHA512

        HashAlgorithm.SHA3_224 -> SHA3_224

        HashAlgorithm.SHA3_256 ->
            SHA3_256

        HashAlgorithm.SHA3_384 ->
            SHA3_384

        HashAlgorithm.SHA3_512 ->
            SHA3_512

        else -> throw IllegalArgumentException("Unsupported hash algorithm \"$algorithm\"")
    },
)

private val _MD5: Hasher by lazy { hasher(MD5) }

private val _SHA1: Hasher by lazy { hasher(SHA1) }

private val _SHA224: Hasher by lazy { hasher(SHA224) }

private val _SHA256: Hasher by lazy { hasher(SHA256) }

private val _SHA384: Hasher by lazy { hasher(SHA384) }

private val _SHA512: Hasher by lazy { hasher(SHA512) }

private val _SHA3_224: Hasher by lazy { hasher(SHA3_224) }

private val _SHA3_256: Hasher by lazy { hasher(SHA3_256) }

private val _SHA3_384: Hasher by lazy { hasher(SHA3_384) }

private val _SHA3_512: Hasher by lazy { hasher(SHA3_512) }

public suspend fun ByteArray.hashMd5(): ByteArray = _MD5.hash(this)

public fun ByteArray.hashMd5Blocking(): ByteArray = _MD5.hashBlocking(this)

public suspend fun ByteArray.hashSha1(): ByteArray = _SHA1.hash(this)

public fun ByteArray.hashSha1Blocking(): ByteArray = _SHA1.hashBlocking(this)

public suspend fun ByteArray.hashSha224(): ByteArray = _SHA224.hash(this)

public fun ByteArray.hashSha224Blocking(): ByteArray = _SHA224.hashBlocking(this)

public suspend fun ByteArray.hashSha256(): ByteArray = _SHA256.hash(this)

public fun ByteArray.hashSha256Blocking(): ByteArray = _SHA256.hashBlocking(this)

public suspend fun ByteArray.hashSha384(): ByteArray = _SHA384.hash(this)

public fun ByteArray.hashSha384Blocking(): ByteArray = _SHA384.hashBlocking(this)

public suspend fun ByteArray.hashSha512(): ByteArray = _SHA512.hash(this)

public fun ByteArray.hashSha512Blocking(): ByteArray = _SHA512.hashBlocking(this)

public suspend fun ByteArray.hashSha3224(): ByteArray = _SHA3_224.hash(this)

public fun ByteArray.hashSha3224Blocking(): ByteArray = _SHA3_224.hashBlocking(this)

public suspend fun ByteArray.hashSha3256(): ByteArray = _SHA3_256.hash(this)

public fun ByteArray.hashSha3256Blocking(): ByteArray = _SHA3_256.hashBlocking(this)

public suspend fun ByteArray.hashSha3384(): ByteArray = _SHA3_384.hash(this)

public fun ByteArray.hashSha3384Blocking(): ByteArray = _SHA3_384.hashBlocking(this)

public suspend fun ByteArray.hashSha3512(): ByteArray = _SHA3_512.hash(this)

public fun ByteArray.hashSha3512Blocking(): ByteArray = _SHA3_512.hashBlocking(this)
