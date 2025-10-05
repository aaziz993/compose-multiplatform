package klib.data.cryptography.pgp.model

import kotlinx.serialization.Serializable

@Serializable
internal data class CreateCleartextMessageOptions(
    var text: String
)
