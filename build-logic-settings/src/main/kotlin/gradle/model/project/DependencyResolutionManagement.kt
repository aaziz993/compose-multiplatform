package gradle.model.project

import gradle.model.repository.ArtifactRepository
import kotlinx.serialization.Serializable

@Serializable
internal data class DependencyResolutionManagement(
    val repositories: List<ArtifactRepository>? = null,
    val versionCatalogs: Set<VersionCatalog>? = null,
)
