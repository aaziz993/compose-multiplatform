package klib.data.config.auth

import klib.data.auth.otp.model.OtpConfig
import klib.data.auth.otp.model.TotpConfig
import kotlinx.serialization.Serializable

@Serializable
public data class AuthConfig(
    val otp: OtpConfig = TotpConfig.DEFAULT,
)
