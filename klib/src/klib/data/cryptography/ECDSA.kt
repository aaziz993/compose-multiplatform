package klib.data.cryptography

import dev.whyoleg.cryptography.CryptographyAlgorithmId
import dev.whyoleg.cryptography.CryptographyProvider
import dev.whyoleg.cryptography.algorithms.EC.Curve
import dev.whyoleg.cryptography.algorithms.EC.PublicKey
import dev.whyoleg.cryptography.algorithms.ECDSA as CryptoECDSA
import dev.whyoleg.cryptography.algorithms.ECDSA.SignatureFormat
import dev.whyoleg.cryptography.algorithms.Digest
import dev.whyoleg.cryptography.materials.key.KeyDecoder
import dev.whyoleg.cryptography.materials.key.KeyGenerator

public val ECDSA: CryptoECDSA by lazy { CryptographyProvider.Default.get(CryptoECDSA) }

public val ECDSA_P256: KeyGenerator<CryptoECDSA.KeyPair> by lazy { ECDSA.keyPairGenerator(Curve.P256) }

private val ECDSA_P256_KEY: KeyDecoder<PublicKey.Format, CryptoECDSA.PublicKey> by lazy {
    ECDSA.publicKeyDecoder(Curve.P256)
}

public val ECDSA_P384: KeyGenerator<CryptoECDSA.KeyPair> by lazy {
    ECDSA.keyPairGenerator(Curve.P384)
}

private val ECDSA_P384_KEY: KeyDecoder<PublicKey.Format, CryptoECDSA.PublicKey> by lazy {
    ECDSA.publicKeyDecoder(Curve.P384)
}

public val ECDSA_P521: KeyGenerator<CryptoECDSA.KeyPair> by lazy {
    ECDSA.keyPairGenerator(Curve.P521)
}

private val ECDSA_P521_KEY: KeyDecoder<PublicKey.Format, CryptoECDSA.PublicKey> by lazy {
    ECDSA.publicKeyDecoder(Curve.P521)
}

public suspend fun ByteArray.decodeECDSAP256PublicKey(
    format: PublicKey.Format = PublicKey.Format.RAW
): CryptoECDSA.PublicKey = ECDSA_P256_KEY.decodeFromByteArray(format, this)

public fun ByteArray.decodeECDSAP256PublicKeyBlocking(
    format: PublicKey.Format = PublicKey.Format.RAW
): CryptoECDSA.PublicKey = ECDSA_P256_KEY.decodeFromByteArrayBlocking(format, this)

public suspend fun ByteArray.decodeECDSAP384PublicKey(
    format: PublicKey.Format = PublicKey.Format.RAW
): CryptoECDSA.PublicKey = ECDSA_P384_KEY.decodeFromByteArray(format, this)

public fun ByteArray.decodeECDSAP384PublicKeyBlocking(
    format: PublicKey.Format = PublicKey.Format.RAW
): CryptoECDSA.PublicKey = ECDSA_P384_KEY.decodeFromByteArrayBlocking(format, this)

public suspend fun ByteArray.decodeECDSAP521PublicKey(
    format: PublicKey.Format = PublicKey.Format.RAW
): CryptoECDSA.PublicKey = ECDSA_P521_KEY.decodeFromByteArray(format, this)

public fun ByteArray.decodeECDSAP521PublicKeyBlocking(
    format: PublicKey.Format = PublicKey.Format.RAW
): CryptoECDSA.PublicKey = ECDSA_P521_KEY.decodeFromByteArrayBlocking(format, this)

public suspend fun ByteArray.generateECDSAP521Signature(
    key: CryptoECDSA.PrivateKey,
    digest: CryptographyAlgorithmId<Digest>,
    format: SignatureFormat = SignatureFormat.RAW,
): ByteArray = key.signatureGenerator(digest, format).generateSignature(this)

public fun ByteArray.generateECDSAP521SignatureBlocking(
    key: CryptoECDSA.PrivateKey,
    digest: CryptographyAlgorithmId<Digest>,
    format: SignatureFormat = SignatureFormat.RAW,
): ByteArray = key.signatureGenerator(digest, format).generateSignatureBlocking(this)

public suspend fun ByteArray.verifyECDSAP521Signature(
    key: CryptoECDSA.PublicKey,
    signature: ByteArray,
    digest: CryptographyAlgorithmId<Digest>,
    format: SignatureFormat = SignatureFormat.RAW,
): Unit = key.signatureVerifier(digest, format).verifySignature(this, signature)

public fun ByteArray.verifyECDSAP521SignatureBlocking(
    key: CryptoECDSA.PublicKey,
    signature: ByteArray,
    digest: CryptographyAlgorithmId<Digest>,
    format: SignatureFormat = SignatureFormat.RAW,
): Unit = key.signatureVerifier(digest, format).verifySignatureBlocking(this, signature)
