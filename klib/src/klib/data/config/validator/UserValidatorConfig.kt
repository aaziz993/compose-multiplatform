package klib.data.config.validator

import klib.data.validator.Validator
import kotlinx.serialization.Serializable

@Serializable
public data class UserValidatorConfig(
    val username: Validator = Validator.nonEmpty(),
    val firstName: Validator = Validator.nonEmpty(),
    val lastName: Validator = Validator.nonEmpty(),
    val phone: Validator = Validator.numericPhone(false),
    val email: Validator = Validator.email(),
    val attributes: Map<String, List<Validator>> = emptyMap()
)
