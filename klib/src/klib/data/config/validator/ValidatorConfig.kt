package klib.data.config.validator

import kotlinx.serialization.Serializable

@Serializable
public data class ValidatorConfig(
    val user: UserValidatorConfig = UserValidatorConfig(),
)
