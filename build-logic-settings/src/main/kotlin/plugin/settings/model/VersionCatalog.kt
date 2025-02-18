package plugin.settings.model

import kotlinx.serialization.Serializable
import plugin.model.dependency.Dependency

@Serializable
internal data class VersionCatalog(
    val name: String,
    val from: Dependency,
)
