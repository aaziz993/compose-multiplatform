package gradle.plugins.signing

import kotlinx.serialization.Serializable

@Serializable
internal data class ClassifierFile(
    val classifier: String,
    val files: List<String>
)
