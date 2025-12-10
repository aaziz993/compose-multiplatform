package klib.data.auth.otp.model

import dev.whyoleg.cryptography.algorithms.Digest
import dev.whyoleg.cryptography.algorithms.SHA1
import dev.whyoleg.cryptography.algorithms.SHA512
import klib.data.cryptography.model.CryptographyAlgorithmIdSerial
import klib.data.type.serialization.serializers.transform.MapTransformingPolymorphicSerializer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(OtpConfigSerializer::class)
public sealed class OtpConfig {

    public abstract val codeDigits: Int
    public abstract val hmacAlgorithm: CryptographyAlgorithmIdSerial<Digest>
}

private object OtpConfigSerializer : MapTransformingPolymorphicSerializer<OtpConfig>(
    baseClass = OtpConfig::class,
    subclasses = mapOf(
        HotpConfig::class to HotpConfig.serializer(),
        TotpConfig::class to TotpConfig.serializer(),
    ),
)

public fun OtpConfig.isValidCode(code: String): Boolean =
    code.length == codeDigits && code.all { it.isDigit() }

@Serializable
@SerialName("hotp")
public data class HotpConfig(
    override val codeDigits: Int,
    override val hmacAlgorithm: CryptographyAlgorithmIdSerial<Digest>,
) : OtpConfig() {

    public companion object {

        public val DEFAULT: HotpConfig get() = HotpConfig(6, SHA1)
    }
}

@Serializable
@SerialName("totp")
public data class TotpConfig(
    val period: Duration,
    override val codeDigits: Int,
    override val hmacAlgorithm: CryptographyAlgorithmIdSerial<Digest> = SHA512,
) : OtpConfig() {

    public companion object {

        public val DEFAULT: TotpConfig get() = TotpConfig(30.seconds, 6, SHA1)
    }
}

internal val Duration.totpMillis: Long
    get() = Instant
        .fromEpochMilliseconds(0L)
        .plus(this)
        .toEpochMilliseconds()

public fun TotpConfig.toHotpConfig(): HotpConfig = HotpConfig(codeDigits, hmacAlgorithm)
