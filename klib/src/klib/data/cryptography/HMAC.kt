package klib.data.cryptography

import dev.whyoleg.cryptography.CryptographyProvider
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
import dev.whyoleg.cryptography.materials.key.KeyDecoder
import dev.whyoleg.cryptography.materials.key.KeyGenerator
import dev.whyoleg.cryptography.algorithms.HMAC as CryptoHMAC

public val HMAC: CryptoHMAC by lazy { CryptographyProvider.Default.get(CryptoHMAC) }

public val HMAC_MD5: KeyGenerator<CryptoHMAC.Key> by lazy { HMAC.keyGenerator(MD5) }

private val HMAC_MD5_KEY: KeyDecoder<CryptoHMAC.Key.Format, CryptoHMAC.Key> by lazy { HMAC.keyDecoder(MD5) }

public val HMAC_SHA1: KeyGenerator<CryptoHMAC.Key> by lazy { HMAC.keyGenerator(SHA1) }

private val HMAC_SHA1_KEY: KeyDecoder<CryptoHMAC.Key.Format, CryptoHMAC.Key> by lazy { HMAC.keyDecoder(SHA1) }

public val HMAC_SHA224: KeyGenerator<CryptoHMAC.Key> by lazy { HMAC.keyGenerator(SHA224) }

private val HMAC_SHA224_KEY: KeyDecoder<CryptoHMAC.Key.Format, CryptoHMAC.Key> by lazy {
    HMAC.keyDecoder(SHA224)
}

public val HMAC_SHA256: KeyGenerator<CryptoHMAC.Key> by lazy { HMAC.keyGenerator(SHA256) }

private val HMAC_SHA256_KEY: KeyDecoder<CryptoHMAC.Key.Format, CryptoHMAC.Key> by lazy {
    HMAC.keyDecoder(SHA256)
}

public val HMAC_SHA384: KeyGenerator<CryptoHMAC.Key> by lazy { HMAC.keyGenerator(SHA384) }

private val HMAC_SHA384_KEY: KeyDecoder<CryptoHMAC.Key.Format, CryptoHMAC.Key> by lazy {
    HMAC.keyDecoder(SHA384)
}

public val HMAC_SHA512: KeyGenerator<CryptoHMAC.Key> by lazy { HMAC.keyGenerator(SHA512) }

private val HMAC_SHA512_KEY: KeyDecoder<CryptoHMAC.Key.Format, CryptoHMAC.Key> by lazy {
    HMAC.keyDecoder(SHA512)
}

public val HMAC_SHA3224: KeyGenerator<CryptoHMAC.Key> by lazy { HMAC.keyGenerator(SHA3_224) }

private val HMAC_SHA3224_KEY: KeyDecoder<CryptoHMAC.Key.Format, CryptoHMAC.Key> by lazy {
    HMAC.keyDecoder(SHA3_224)
}

public val HMAC_SHA3256: KeyGenerator<CryptoHMAC.Key> by lazy { HMAC.keyGenerator(SHA3_256) }

private val HMAC_SHA3256_KEY: KeyDecoder<CryptoHMAC.Key.Format, CryptoHMAC.Key> by lazy {
    HMAC.keyDecoder(SHA3_256)
}

public val HMAC_SHA3384: KeyGenerator<CryptoHMAC.Key> by lazy { HMAC.keyGenerator(SHA3_384) }

private val HMAC_SHA3384_KEY: KeyDecoder<CryptoHMAC.Key.Format, CryptoHMAC.Key> by lazy {
    HMAC.keyDecoder(SHA3_384)
}

public val HMAC_SHA3512: KeyGenerator<CryptoHMAC.Key> by lazy { HMAC.keyGenerator(SHA3_512) }

private val HMAC_SHA3512_KEY: KeyDecoder<CryptoHMAC.Key.Format, CryptoHMAC.Key> by lazy {
    HMAC.keyDecoder(SHA3_512)
}

public suspend fun ByteArray.decodeHMACMd5Key(format: CryptoHMAC.Key.Format): CryptoHMAC.Key =
    HMAC_SHA1_KEY.decodeFromByteArray(format, this)

public suspend fun ByteArray.decodeHMACSha1Key(format: CryptoHMAC.Key.Format): CryptoHMAC.Key =
    HMAC_MD5_KEY.decodeFromByteArray(format, this)

public suspend fun ByteArray.decodeHMACSha224Key(format: CryptoHMAC.Key.Format): CryptoHMAC.Key =
    HMAC_SHA224_KEY.decodeFromByteArray(format, this)

public fun ByteArray.decodeHMACSha224KeyBlocking(format: CryptoHMAC.Key.Format): CryptoHMAC.Key =
    HMAC_SHA224_KEY.decodeFromByteArrayBlocking(format, this)

public suspend fun ByteArray.decodeHMACSha256Key(format: CryptoHMAC.Key.Format): CryptoHMAC.Key =
    HMAC_SHA256_KEY.decodeFromByteArray(format, this)

public fun ByteArray.decodeHMACSha256KeyBlocking(format: CryptoHMAC.Key.Format): CryptoHMAC.Key =
    HMAC_SHA256_KEY.decodeFromByteArrayBlocking(format, this)

public suspend fun ByteArray.decodeHMACSha384Key(format: CryptoHMAC.Key.Format): CryptoHMAC.Key =
    HMAC_SHA384_KEY.decodeFromByteArray(format, this)

public fun ByteArray.decodeHMACSha384KeyBlocking(format: CryptoHMAC.Key.Format): CryptoHMAC.Key =
    HMAC_SHA384_KEY.decodeFromByteArrayBlocking(format, this)

public suspend fun ByteArray.decodeHMACSha512Key(format: CryptoHMAC.Key.Format): CryptoHMAC.Key =
    HMAC_SHA512_KEY.decodeFromByteArray(format, this)

public fun ByteArray.decodeHMACSha512KeyBlocking(format: CryptoHMAC.Key.Format): CryptoHMAC.Key =
    HMAC_SHA512_KEY.decodeFromByteArrayBlocking(format, this)

public suspend fun ByteArray.decodeHMACSha3224Key(format: CryptoHMAC.Key.Format): CryptoHMAC.Key =
    HMAC_SHA3224_KEY.decodeFromByteArray(format, this)

public fun ByteArray.decodeHMACSha3224KeyBlocking(format: CryptoHMAC.Key.Format): CryptoHMAC.Key =
    HMAC_SHA3224_KEY.decodeFromByteArrayBlocking(format, this)

public suspend fun ByteArray.decodeHMACSha3256Key(format: CryptoHMAC.Key.Format): CryptoHMAC.Key =
    HMAC_SHA3256_KEY.decodeFromByteArray(format, this)

public fun ByteArray.decodeHMACSha3256KeyBlocking(format: CryptoHMAC.Key.Format): CryptoHMAC.Key =
    HMAC_SHA3256_KEY.decodeFromByteArrayBlocking(format, this)

public suspend fun ByteArray.decodeHMACSha3384Key(format: CryptoHMAC.Key.Format): CryptoHMAC.Key =
    HMAC_SHA3384_KEY.decodeFromByteArray(format, this)

public fun ByteArray.decodeHMACSha3384KeyBlocking(format: CryptoHMAC.Key.Format): CryptoHMAC.Key =
    HMAC_SHA3384_KEY.decodeFromByteArrayBlocking(format, this)

public suspend fun ByteArray.decodeHMACSha3512Key(format: CryptoHMAC.Key.Format): CryptoHMAC.Key =
    HMAC_SHA3512_KEY.decodeFromByteArray(format, this)

public fun ByteArray.decodeHMACSha3512KeyBlocking(format: CryptoHMAC.Key.Format): CryptoHMAC.Key =
    HMAC_SHA3512_KEY.decodeFromByteArrayBlocking(format, this)

public suspend fun ByteArray.generateHMACSignature(key: CryptoHMAC.Key): ByteArray =
    key.signatureGenerator().generateSignature(this)

public fun ByteArray.generateHMACSignatureBlocking(key: CryptoHMAC.Key): ByteArray =
    key.signatureGenerator().generateSignatureBlocking(this)

public suspend fun ByteArray.verifyHMACSignature(key: CryptoHMAC.Key, signature: ByteArray): Unit =
    key.signatureVerifier().verifySignature(this, signature)

public fun ByteArray.verifyHMACSignatureBlocking(key: CryptoHMAC.Key, signature: ByteArray): Unit =
    key.signatureVerifier().verifySignatureBlocking(this, signature)
