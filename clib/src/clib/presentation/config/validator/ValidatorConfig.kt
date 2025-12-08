package clib.presentation.config.validator

import klib.data.config.validator.UserValidatorConfig
import klib.data.config.validator.ValidatorConfig
import kotlinx.serialization.Serializable

@Serializable
public data class ValidatorConfig(
    override val user: UserValidatorConfig = UserValidatorConfig(),
) : ValidatorConfig
