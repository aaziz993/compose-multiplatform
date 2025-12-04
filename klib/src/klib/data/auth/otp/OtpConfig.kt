package klib.data.auth.otp

import kotlinx.serialization.Serializable

@Serializable
public data class HotpConfig(
    val codeDigits: OtpDigits,
    val hmacAlgorithm: HmacAlgorithm,
) {
    public companion object {
        public val DEFAULT: HotpConfig get() = HotpConfig(OtpDigits.Six, HmacAlgorithm.SHA1)
    }
}

@Serializable
public data class TotpConfig(
    val period: TotpPeriod,
    val codeDigits: OtpDigits,
    val hmacAlgorithm: HmacAlgorithm,
) {
    public companion object {
        public val DEFAULT: TotpConfig get() = TotpConfig(TotpPeriod.Thirty, OtpDigits.Six, HmacAlgorithm.SHA1)
    }
}

public fun TotpConfig.toHotpConfig(): HotpConfig = HotpConfig(codeDigits, hmacAlgorithm)
