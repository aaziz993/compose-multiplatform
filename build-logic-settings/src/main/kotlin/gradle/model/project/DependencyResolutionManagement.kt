package gradle.model.project

import kotlinx.serialization.Serializable

@Serializable
internal data class DependencyResolutionManagement(
    val repositories: List<String>? = null,
    val versionCatalogs: Set<VersionCatalog>? = null,
)
