package gradle.api.initialization

import gradle.api.artifacts.dsl.RepositoryHandler
import gradle.api.project.VersionCatalog
import kotlinx.serialization.Serializable

@Serializable
internal data class DependencyResolutionManagement(
    val repositories: RepositoryHandler? = null,
    val versionCatalogs: LinkedHashSet<VersionCatalog>? = null,
)
