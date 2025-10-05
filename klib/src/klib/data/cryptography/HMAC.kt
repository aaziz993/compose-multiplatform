package klib.data.cryptography

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
import dev.whyoleg.cryptography.algorithms.HMAC
import dev.whyoleg.cryptography.materials.key.KeyDecoder
import dev.whyoleg.cryptography.materials.key.KeyGenerator

private val _HMAC: HMAC by lazy { CryptographyProvider.Default.get(HMAC) }

private fun hmacKeyGenerator(digest: CryptographyAlgorithmId<Digest>): KeyGenerator<HMAC.Key> = _HMAC.keyGenerator(digest)

private fun hmacKeyDecoder(digest: CryptographyAlgorithmId<Digest>): KeyDecoder<HMAC.Key.Format, HMAC.Key> = _HMAC.keyDecoder(digest)

public val HMAC_MD5: KeyGenerator<HMAC.Key> by lazy { hmacKeyGenerator(MD5) }

private val HMAC_MD5_KEY: KeyDecoder<HMAC.Key.Format, HMAC.Key> by lazy { hmacKeyDecoder(MD5) }

public val HMAC_SHA1: KeyGenerator<HMAC.Key> by lazy { hmacKeyGenerator(SHA1) }

private val HMAC_SHA1_KEY: KeyDecoder<HMAC.Key.Format, HMAC.Key> by lazy { hmacKeyDecoder(SHA1) }

public val HMAC_SHA224: KeyGenerator<HMAC.Key> by lazy { hmacKeyGenerator(SHA224) }

private val HMAC_SHA224_KEY: KeyDecoder<HMAC.Key.Format, HMAC.Key> by lazy { hmacKeyDecoder(SHA224) }

public val HMAC_SHA256: KeyGenerator<HMAC.Key> by lazy { hmacKeyGenerator(SHA256) }

private val HMAC_SHA256_KEY: KeyDecoder<HMAC.Key.Format, HMAC.Key> by lazy { hmacKeyDecoder(SHA256) }

public val HMAC_SHA384: KeyGenerator<HMAC.Key> by lazy { hmacKeyGenerator(SHA384) }

private val HMAC_SHA384_KEY: KeyDecoder<HMAC.Key.Format, HMAC.Key> by lazy { hmacKeyDecoder(SHA384) }

public val HMAC_SHA512: KeyGenerator<HMAC.Key> by lazy { hmacKeyGenerator(SHA512) }

private val HMAC_SHA512_KEY: KeyDecoder<HMAC.Key.Format, HMAC.Key> by lazy { hmacKeyDecoder(SHA512) }

public val HMAC_SHA3224: KeyGenerator<HMAC.Key> by lazy { hmacKeyGenerator(SHA3_224) }

private val HMAC_SHA3224_KEY: KeyDecoder<HMAC.Key.Format, HMAC.Key> by lazy { hmacKeyDecoder(SHA3_224) }

public val HMAC_SHA3256: KeyGenerator<HMAC.Key> by lazy { hmacKeyGenerator(SHA3_256) }

private val HMAC_SHA3256_KEY: KeyDecoder<HMAC.Key.Format, HMAC.Key> by lazy { hmacKeyDecoder(SHA3_256) }

public val HMAC_SHA3384: KeyGenerator<HMAC.Key> by lazy { hmacKeyGenerator(SHA3_384) }

private val HMAC_SHA3384_KEY: KeyDecoder<HMAC.Key.Format, HMAC.Key> by lazy { hmacKeyDecoder(SHA3_384) }

public val HMAC_SHA3512: KeyGenerator<HMAC.Key> by lazy { hmacKeyGenerator(SHA3_512) }

private val HMAC_SHA3512_KEY: KeyDecoder<HMAC.Key.Format, HMAC.Key> by lazy { hmacKeyDecoder(SHA3_512) }

public suspend fun ByteArray.decodeHMACMd5Key(format: HMAC.Key.Format): HMAC.Key =
    HMAC_SHA1_KEY.decodeFromByteArray(format, this)

public suspend fun ByteArray.decodeHMACSha1Key(format: HMAC.Key.Format): HMAC.Key =
    HMAC_MD5_KEY.decodeFromByteArray(format, this)

public suspend fun ByteArray.decodeHMACSha224Key(format: HMAC.Key.Format): HMAC.Key =
    HMAC_SHA224_KEY.decodeFromByteArray(format, this)

public fun ByteArray.decodeHMACSha224KeyBlocking(format: HMAC.Key.Format): HMAC.Key =
    HMAC_SHA224_KEY.decodeFromByteArrayBlocking(format, this)

public suspend fun ByteArray.decodeHMACSha256Key(format: HMAC.Key.Format): HMAC.Key =
    HMAC_SHA256_KEY.decodeFromByteArray(format, this)

public fun ByteArray.decodeHMACSha256KeyBlocking(format: HMAC.Key.Format): HMAC.Key =
    HMAC_SHA256_KEY.decodeFromByteArrayBlocking(format, this)

public suspend fun ByteArray.decodeHMACSha384Key(format: HMAC.Key.Format): HMAC.Key =
    HMAC_SHA384_KEY.decodeFromByteArray(format, this)

public fun ByteArray.decodeHMACSha384KeyBlocking(format: HMAC.Key.Format): HMAC.Key =
    HMAC_SHA384_KEY.decodeFromByteArrayBlocking(format, this)

public suspend fun ByteArray.decodeHMACSha512Key(format: HMAC.Key.Format): HMAC.Key =
    HMAC_SHA512_KEY.decodeFromByteArray(format, this)

public fun ByteArray.decodeHMACSha512KeyBlocking(format: HMAC.Key.Format): HMAC.Key =
    HMAC_SHA512_KEY.decodeFromByteArrayBlocking(format, this)

public suspend fun ByteArray.decodeHMACSha3224Key(format: HMAC.Key.Format): HMAC.Key =
    HMAC_SHA3224_KEY.decodeFromByteArray(format, this)

public fun ByteArray.decodeHMACSha3224KeyBlocking(format: HMAC.Key.Format): HMAC.Key =
    HMAC_SHA3224_KEY.decodeFromByteArrayBlocking(format, this)

public suspend fun ByteArray.decodeHMACSha3256Key(format: HMAC.Key.Format): HMAC.Key =
    HMAC_SHA3256_KEY.decodeFromByteArray(format, this)

public fun ByteArray.decodeHMACSha3256KeyBlocking(format: HMAC.Key.Format): HMAC.Key =
    HMAC_SHA3256_KEY.decodeFromByteArrayBlocking(format, this)

public suspend fun ByteArray.decodeHMACSha3384Key(format: HMAC.Key.Format): HMAC.Key =
    HMAC_SHA3384_KEY.decodeFromByteArray(format, this)

public fun ByteArray.decodeHMACSha3384KeyBlocking(format: HMAC.Key.Format): HMAC.Key =
    HMAC_SHA3384_KEY.decodeFromByteArrayBlocking(format, this)

public suspend fun ByteArray.decodeHMACSha3512Key(format: HMAC.Key.Format): HMAC.Key =
    HMAC_SHA3512_KEY.decodeFromByteArray(format, this)

public fun ByteArray.decodeHMACSha3512KeyBlocking(format: HMAC.Key.Format): HMAC.Key =
    HMAC_SHA3512_KEY.decodeFromByteArrayBlocking(format, this)

public suspend fun ByteArray.generateHMACSignature(key: HMAC.Key): ByteArray =
    key.signatureGenerator().generateSignature(this)

public fun ByteArray.generateHMACSignatureBlocking(key: HMAC.Key): ByteArray =
    key.signatureGenerator().generateSignatureBlocking(this)

public suspend fun ByteArray.verifyHMACSignature(key: HMAC.Key, signature: ByteArray): Unit =
    key.signatureVerifier().verifySignature(this, signature)

public fun ByteArray.verifyHMACSignatureBlocking(key: HMAC.Key, signature: ByteArray): Unit =
    key.signatureVerifier().verifySignatureBlocking(this, signature)
