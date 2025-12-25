package klib.data.config.auth

import kotlinx.serialization.Serializable

@Serializable
public data class PinCodeConfig(
    val length: Int = 4,
    val phoneReset: Boolean = true,
    val emailReset: Boolean = true,
)
