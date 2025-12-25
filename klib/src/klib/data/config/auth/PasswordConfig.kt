package klib.data.config.auth

import klib.data.validator.Validator
import kotlinx.serialization.Serializable

@Serializable
public data class PasswordConfig(
    val phoneReset: Boolean = true,
    val emailReset: Boolean = true,
    val validator: Validator? = null,
)
