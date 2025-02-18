package plugin.settings.model

import kotlinx.serialization.Serializable
import plugin.model.Dependency

@Serializable
internal data class VersionCatalog(
    val name: String,
    val from: Dependency,
)
