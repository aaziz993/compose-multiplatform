package plugin.settings.model

import kotlinx.serialization.Serializable

@Serializable
internal data class DependencyResolutionManagement(
    val versionCatalogs: List<VersionCatalog> = emptyList(),
)
