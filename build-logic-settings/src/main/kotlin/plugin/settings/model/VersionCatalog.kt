package plugin.settings.model

import kotlinx.serialization.Serializable

@Serializable
internal data class VersionCatalog(
    val name: String,
    val files: List<String>? = null,
    val dependency: String? = null
)
