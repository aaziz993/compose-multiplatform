package klib.auth.otp.model

import dev.whyoleg.cryptography.algorithms.Digest
import dev.whyoleg.cryptography.algorithms.SHA1
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
    public abstract val window: Int
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
    override val codeDigits: Int = 6,
    override val hmacAlgorithm: CryptographyAlgorithmIdSerial<Digest> = SHA1,
    override val window: Int = 5,
) : OtpConfig()

@Serializable
@SerialName("totp")
public data class TotpConfig(
    val period: Duration = 30.seconds,
    override val codeDigits: Int = 6,
    override val hmacAlgorithm: CryptographyAlgorithmIdSerial<Digest> = SHA1,
    override val window: Int = 1,
) : OtpConfig()

internal val Duration.totpMillis: Long
    get() = Instant
        .fromEpochMilliseconds(0L)
        .plus(this)
        .toEpochMilliseconds()

public fun TotpConfig.toHotpConfig(): HotpConfig = HotpConfig(codeDigits, hmacAlgorithm, window)
