package klib.data.auth.otp

import kotlinx.serialization.Serializable

@Serializable
public enum class OtpType {

    TOTP, HOTP;
}
