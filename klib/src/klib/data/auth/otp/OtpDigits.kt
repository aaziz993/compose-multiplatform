package klib.data.auth.otp

import kotlinx.serialization.Serializable

@Serializable
public enum class OtpDigits(public val number: Int) {
    Six(6), Eight(8);
}



