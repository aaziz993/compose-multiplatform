package plugin.project.model

import kotlinx.serialization.Serializable

@Serializable
internal data class DependencyResolutionManagement(
    val repositories: List<String>? = null,
    val versionCatalogs: Set<VersionCatalog>? = null,
)
