package plugin.project.settings.model

import kotlinx.serialization.Serializable

@Serializable
internal data class VersionCatalog(
    val name: String,
    val from: List<String>
)
