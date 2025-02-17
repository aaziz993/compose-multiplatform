package plugin.project.model.project

import kotlinx.serialization.Serializable

@Serializable
internal data class DependencyResolutionManagement(
    val versionCatalogs: List<VersionCatalog> = emptyList(),
)
