package plugin.settings.model

import kotlinx.serialization.Serializable

@Serializable
internal data class DependencyResolutionManagement(
    val repositories: List<String>? = null,
    val versionCatalogs: List<VersionCatalog>? = null,
)
