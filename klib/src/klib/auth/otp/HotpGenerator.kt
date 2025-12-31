package klib.auth.otp

import klib.auth.otp.model.HotpConfig
import klib.auth.otp.model.isValidCode
import klib.data.cryptography.HMAC
import klib.data.type.primitives.number.toByteArray
import klib.data.type.primitives.number.toInt
import klib.data.type.primitives.string.encoding.decodeBase32ToBytes
import kotlin.experimental.and
import kotlin.math.pow
import dev.whyoleg.cryptography.algorithms.HMAC as CryptoHMAC

public class HotpGenerator(
    secret: String,
    private val config: HotpConfig
) {

    private val hmacKey = HMAC.keyDecoder(config.hmacAlgorithm)
        .decodeFromByteArrayBlocking(CryptoHMAC.Key.Format.RAW, secret.decodeBase32ToBytes()!!)

    private val otpModulus = 10.0.pow(config.codeDigits).toInt()

    public fun generate(count: Long): String {
        val message = count.toByteArray(8)

        val hash = hmacKey.signatureGenerator().generateSignatureBlocking(message)

        val offset = hash.last().and(0x0F).toInt()

        val binary = ByteArray(4) { i -> hash[i + offset] }

        binary[0] = binary[0] and 0x7F

        val codeInt = binary.toInt() % otpModulus

        return codeInt.toString().padStart(config.codeDigits, '0')
    }

    /**
     * RFC 4226 HOTP verification
     *
     * @param code user provided OTP.
     * @param counter current counter.
     */
    public fun verify(
        code: String,
        counter: Long,
    ): Boolean {
        if (!config.isValidCode(code)) return false
        if (counter < 0) return false

        for (i in 0..config.window) {
            val count = counter + i
            if (count < 0) continue
            if (constantTimeEquals(generate(count), code)) return true
        }
        return false
    }

    private fun constantTimeEquals(a: String, b: String): Boolean {
        if (a.length != b.length) return false
        var result = 0
        for (i in a.indices) {
            result = result or (a[i].code xor b[i].code)
        }
        return result == 0
    }
}
