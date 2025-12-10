package klib.data.auth.otp

import klib.data.auth.otp.model.HotpConfig
import klib.data.cryptography.HMAC
import klib.data.type.primitives.number.toByteArray
import klib.data.type.primitives.number.toInt
import klib.data.type.primitives.string.encodeToByteArray
import klib.data.type.primitives.string.encoding.decodeBase32
import klib.data.type.primitives.string.encoding.decodeBase32ToBytes
import kotlin.experimental.and
import kotlin.math.pow
import dev.whyoleg.cryptography.algorithms.HMAC as CryptoHMAC

public class HotpGenerator(
    secret: String,
    private val config: HotpConfig
) {

    private val secret: ByteArray = secret.decodeBase32ToBytes()!!

    public fun generate(count: Long): String {
        val message = count.toByteArray(8)

        val hmacKey = HMAC.keyDecoder(config.hmacAlgorithm)
            .decodeFromByteArrayBlocking(CryptoHMAC.Key.Format.RAW, secret)
        val hash = hmacKey.signatureGenerator().generateSignatureBlocking(message)

        val offset = hash.last().and(0x0F).toInt()

        val binary = ByteArray(4) { i -> hash[i + offset] }

        binary[0] = binary[0] and 0x7F

        val digits = config.codeDigits
        val codeInt = binary.toInt() % 10.0.pow(digits).toInt()

        return codeInt.toString().padStart(digits, '0')
    }
}
