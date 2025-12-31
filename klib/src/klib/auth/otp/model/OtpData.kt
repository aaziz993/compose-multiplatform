package klib.auth.otp.model

import klib.auth.otp.HotpGenerator
import klib.auth.otp.TotpGenerator
import kotlin.uuid.Uuid
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
public sealed class OtpData {

    public abstract val issuer: String?
    public abstract val accountName: String?
    public abstract val secret: String
    public abstract val uuid: Uuid

    public abstract fun code(timestamp: Long): String

    abstract override fun hashCode(): Int

    abstract override fun equals(other: Any?): Boolean

    public val namePresentation: String? by lazy {
        val issuer = issuer?.takeIf { it.isNotBlank() }
        val accountName = accountName
            ?.takeIf { it.isNotBlank() }
            ?.run {
                when {
                    startsWith("$issuer: ") -> removePrefix("$issuer: ")
                    startsWith("$issuer:") -> removePrefix("$issuer:")
                    else -> this
                }
            }
        when {
            issuer != null && accountName != null -> "$issuer: $accountName"
            issuer != null -> issuer
            accountName != null -> accountName
            else -> null
        }
    }
}

@Serializable
public data class HotpData(
    override val issuer: String?,
    override val accountName: String?,
    override val secret: String,
    private val counter: Long,
    private val config: HotpConfig,
    override val uuid: Uuid,
) : OtpData() {

    @Transient
    private val authenticator: HotpGenerator =
        HotpGenerator(secret, config)

    override fun code(timestamp: Long): String =
        authenticator.generate(counter)

    public fun increaseCounter(): HotpData =
        copy(counter = counter + 1)

    public companion object {

        public operator fun invoke(
            issuer: String?,
            accountName: String?,
            secret: String,
            counter: Long,
            config: HotpConfig,
        ): OtpData = HotpData(issuer, accountName, secret, counter, config, Uuid.random())
    }
}

@Serializable
public data class TotpData(
    override val issuer: String?,
    override val accountName: String?,
    override val secret: String,
    private val config: TotpConfig,
    override val uuid: Uuid,
) : OtpData() {

    @Transient
    private val authenticator: TotpGenerator =
        TotpGenerator(secret, config)

    @Transient
    val periodMillis: Int =
        authenticator.config.period.totpMillis.coerceAtMost(Int.MAX_VALUE.toLong()).toInt()

    override fun code(timestamp: Long): String =
        authenticator.generate(timestamp)

    public fun timeslotLeft(timestamp: Long): Double =
        authenticator.timeslotLeft(timestamp)

    public companion object {

        public operator fun invoke(
            issuer: String?,
            accountName: String?,
            secret: String,
            config: TotpConfig,
        ): OtpData = TotpData(issuer, accountName, secret, config, Uuid.random())
    }
}
