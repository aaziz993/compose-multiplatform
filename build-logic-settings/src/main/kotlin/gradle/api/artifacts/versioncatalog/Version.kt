package gradle.api.artifacts.versioncatalog

import kotlinx.serialization.Serializable

@Serializable
internal data class Version(
    val require: String? = null,
    val strictly: String? = null,
    val prefer: String? = null,
    val reject: List<String>? = null,
    val rejectAll: Boolean? = null,
    val ref: String? = null,
)
