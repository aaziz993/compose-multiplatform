package klib.data.type.validator.model

import klib.data.type.validator.Validator
import kotlinx.serialization.Serializable

@Serializable
public data class UserValidator(
    val username: Validator = Validator.nonEmpty(),
    val firstName: Validator = Validator.nonEmpty(),
    val lastName: Validator = Validator.nonEmpty(),
    val phone: Validator = Validator.numericPhone(false),
    val email: Validator = Validator.email(),
)
