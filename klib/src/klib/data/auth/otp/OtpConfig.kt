package klib.data.auth.otp

import dev.whyoleg.cryptography.algorithms.Digest
import dev.whyoleg.cryptography.algorithms.SHA1
import klib.data.cryptography.model.CryptographyAlgorithmIdSerial
import kotlinx.serialization.Serializable

@Serializable
public data class HotpConfig(
    val codeDigits: OtpDigits,
    val hmacAlgorithm: CryptographyAlgorithmIdSerial<Digest>,
) {

    public companion object {

        public val DEFAULT: HotpConfig get() = HotpConfig(OtpDigits.Six, SHA1)
    }
}

@Serializable
public data class TotpConfig(
    val period: TotpPeriod,
    val codeDigits: OtpDigits,
    val hmacAlgorithm: CryptographyAlgorithmIdSerial<Digest>,
) {

    public companion object {

        public val DEFAULT: TotpConfig get() = TotpConfig(TotpPeriod.Thirty, OtpDigits.Six, SHA1)
    }
}

public fun TotpConfig.toHotpConfig(): HotpConfig = HotpConfig(codeDigits, hmacAlgorithm)
