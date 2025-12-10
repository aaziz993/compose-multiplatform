package klib.data.auth.otp

import klib.data.auth.otp.model.TotpConfig
import klib.data.auth.otp.model.isValidCode
import klib.data.auth.otp.model.toHotpConfig
import klib.data.auth.otp.model.totpMillis
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
     * @param window allowed time-step drift (±window)
     */
    public fun verify(
        code: String,
        timestamp: Long,
        window: Int = 1,
    ): Boolean {
        if (!config.isValidCode(code)) return false

        val currentCounter = counter(timestamp)

        for (i in -window..window) {
            val c = currentCounter + i
            if (c < 0) continue
            if (hotpGenerator.verify(code, c)) return true
        }
        return false
    }
}
