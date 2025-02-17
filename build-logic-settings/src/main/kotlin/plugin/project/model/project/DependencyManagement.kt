package plugin.project.model.project

import kotlinx.serialization.Serializable

@Serializable
internal data class DependencyManagement(
    val versionCatalogs: List<VersionCatalog> = emptyList(),
)
