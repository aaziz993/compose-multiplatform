package gradle.plugins.signing

import kotlinx.serialization.Serializable

@Serializable
internal data class SignFile(
    val classifier: String,
    val files: List<String>
)
