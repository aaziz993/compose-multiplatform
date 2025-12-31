package klib.auth.otp

import klib.auth.otp.model.TotpConfig
import klib.auth.otp.model.isValidCode
import klib.auth.otp.model.toHotpConfig
import klib.auth.otp.model.totpMillis
import kotlin.math.floor

public class TotpGenerator(
    secret: String,
    public val config: TotpConfig,
) {

    private val hotpGenerator: HotpGenerator =
        HotpGenerator(secret, config.toHotpConfig())

    private fun counter(timestamp: Long): Long {
        val millis = config.period.totpMillis
        if (millis <= 0L) return 0
        return floor(timestamp.toDouble() / millis).toLong()
    }

    public fun generate(timestamp: Long): String =
        hotpGenerator.generate(counter(timestamp))

    private fun timeslotStart(timestamp: Long): Long {
        val counter = counter(timestamp)
        val timeStepMillis = config.period.totpMillis.toDouble()
        return (counter * timeStepMillis).toLong()
    }

    public fun timeslotLeft(timestamp: Long): Double {
        val diff = timestamp - timeslotStart(timestamp)
        return 1.0 - diff.toDouble() / config.period.totpMillis.toDouble()
    }

    /**
     * RFC 6238 TOTP verification
     *
     * @param code user provided OTP
     * @param timestamp current time in millis
     */
    public fun verify(
        code: String,
        timestamp: Long,
    ): Boolean {
        if (!config.isValidCode(code)) return false

        val currentCounter = counter(timestamp)

        for (i in -config.window..config.window) {
            val counter = currentCounter + i
            if (counter < 0) continue
            if (hotpGenerator.verify(code, counter)) return true
        }
        return false
    }
}
