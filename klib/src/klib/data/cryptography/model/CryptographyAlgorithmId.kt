package klib.data.cryptography.model

import dev.whyoleg.cryptography.CryptographyAlgorithm
import dev.whyoleg.cryptography.algorithms.Digest
import klib.data.type.serialization.serializers.transform.MapTransformingPolymorphicSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import dev.whyoleg.cryptography.CryptographyAlgorithmId as CryptoCryptographyAlgorithmId
import dev.whyoleg.cryptography.algorithms.MD5 as CryptoMD5
import dev.whyoleg.cryptography.algorithms.SHA1 as CryptoSHA1
import dev.whyoleg.cryptography.algorithms.SHA224 as CryptoSHA224
import dev.whyoleg.cryptography.algorithms.SHA256 as CryptoSHA256
import dev.whyoleg.cryptography.algorithms.SHA384 as CryptoSHA384
import dev.whyoleg.cryptography.algorithms.SHA512 as CryptoSHA512
import dev.whyoleg.cryptography.algorithms.SHA3_224 as CryptoSHA3_224
import dev.whyoleg.cryptography.algorithms.SHA3_256 as CryptoSHA3_256
import dev.whyoleg.cryptography.algorithms.SHA3_384 as CryptoSHA3_384
import dev.whyoleg.cryptography.algorithms.SHA3_512 as CryptoSHA3_512
import dev.whyoleg.cryptography.algorithms.RIPEMD160 as CryptoRIPEMD160

@Serializable(CryptographyAlgorithmIdSerializer::class)
private sealed class CryptographyAlgorithmId<T : CryptographyAlgorithm> {

    abstract fun toCryptographyAlgorithmId(): CryptoCryptographyAlgorithmId<T>
}

private class CryptographyAlgorithmIdSerializer(
    @Suppress("Unused") tSerializer: KSerializer<Nothing>
) : MapTransformingPolymorphicSerializer<CryptographyAlgorithmId<*>>(
    baseClass = CryptographyAlgorithmId::class,
    subclasses = mapOf(
        MD5::class to MD5.serializer(),
        SHA1::class to SHA1.serializer(),
        SHA224::class to SHA224.serializer(),
        SHA256::class to SHA256.serializer(),
        SHA384::class to SHA384.serializer(),
        SHA512::class to SHA512.serializer(),
        SHA3224::class to SHA3224.serializer(),
        SHA3256::class to SHA3256.serializer(),
        SHA3384::class to SHA3384.serializer(),
        SHA3512::class to SHA3512.serializer(),
        RIPEMD160::class to RIPEMD160.serializer(),
    ),
)

@Serializable
@SerialName("md5")
private data object MD5 : CryptographyAlgorithmId<Digest>() {

    override fun toCryptographyAlgorithmId(): CryptoCryptographyAlgorithmId<Digest> = CryptoMD5
}

@Serializable
@SerialName("sha1")
private data object SHA1 : CryptographyAlgorithmId<Digest>() {

    override fun toCryptographyAlgorithmId(): CryptoCryptographyAlgorithmId<Digest> = CryptoSHA1
}

@Serializable
@SerialName("sha224")
private data object SHA224 : CryptographyAlgorithmId<Digest>() {

    override fun toCryptographyAlgorithmId(): CryptoCryptographyAlgorithmId<Digest> = CryptoSHA224
}

@Serializable
@SerialName("sha256")
private data object SHA256 : CryptographyAlgorithmId<Digest>() {

    override fun toCryptographyAlgorithmId(): CryptoCryptographyAlgorithmId<Digest> = CryptoSHA256
}

@Serializable
@SerialName("sha384")
private data object SHA384 : CryptographyAlgorithmId<Digest>() {

    override fun toCryptographyAlgorithmId(): CryptoCryptographyAlgorithmId<Digest> = CryptoSHA384
}

@Serializable
@SerialName("sha512")
private data object SHA512 : CryptographyAlgorithmId<Digest>() {

    override fun toCryptographyAlgorithmId(): CryptoCryptographyAlgorithmId<Digest> = CryptoSHA512
}

@Serializable
@SerialName("sha3224")
private data object SHA3224 : CryptographyAlgorithmId<Digest>() {

    override fun toCryptographyAlgorithmId(): CryptoCryptographyAlgorithmId<Digest> = CryptoSHA3_224
}

@Serializable
@SerialName("sha3256")
private data object SHA3256 : CryptographyAlgorithmId<Digest>() {

    override fun toCryptographyAlgorithmId(): CryptoCryptographyAlgorithmId<Digest> = CryptoSHA3_256
}

@Serializable
@SerialName("sha3384")
private data object SHA3384 : CryptographyAlgorithmId<Digest>() {

    override fun toCryptographyAlgorithmId(): CryptoCryptographyAlgorithmId<Digest> = CryptoSHA3_384
}

@Serializable
@SerialName("sha3512")
private data object SHA3512 : CryptographyAlgorithmId<Digest>() {

    override fun toCryptographyAlgorithmId(): CryptoCryptographyAlgorithmId<Digest> = CryptoSHA3_512
}

@Serializable
@SerialName("ripemd160")
private data object RIPEMD160 : CryptographyAlgorithmId<Digest>() {

    override fun toCryptographyAlgorithmId(): CryptoCryptographyAlgorithmId<Digest> = CryptoRIPEMD160
}

public class CryptoCryptographyAlgorithmIdSerializer<T : CryptographyAlgorithm>(
    tSerializer: KSerializer<T>
) : KSerializer<CryptoCryptographyAlgorithmId<T>> {

    private val delegate = CryptographyAlgorithmId.serializer(tSerializer)
    override val descriptor: SerialDescriptor = delegate.descriptor

    @Suppress("UNCHECKED_CAST")
    override fun serialize(encoder: Encoder, value: CryptoCryptographyAlgorithmId<T>): Unit =
        encoder.encodeSerializableValue(
            delegate,
            when (value) {
                CryptoMD5 -> MD5
                CryptoSHA1 -> SHA1
                CryptoSHA224 -> SHA224
                CryptoSHA256 -> SHA256
                CryptoSHA384 -> SHA384
                CryptoSHA512 -> SHA512
                CryptoSHA3_224 -> SHA3224
                CryptoSHA3_256 -> SHA3256
                CryptoSHA3_384 -> SHA3384
                CryptoSHA3_512 -> SHA3512
                CryptoRIPEMD160 -> RIPEMD160

                else -> throw IllegalArgumentException("Unknown corner shape '$value'")
            } as CryptographyAlgorithmId<T>,
        )

    override fun deserialize(decoder: Decoder): CryptoCryptographyAlgorithmId<T> =
        decoder.decodeSerializableValue(delegate).toCryptographyAlgorithmId()
}

public typealias CryptographyAlgorithmIdSerial<T> = @Serializable(with = CryptoCryptographyAlgorithmIdSerializer::class) CryptoCryptographyAlgorithmId<T>
