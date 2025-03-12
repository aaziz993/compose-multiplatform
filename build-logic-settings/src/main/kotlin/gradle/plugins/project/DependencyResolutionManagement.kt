package gradle.plugins.project

import gradle.plugins.repository.ArtifactRepository
import gradle.plugins.repository.ArtifactRepositoryTransformingSerializer
import kotlinx.serialization.Serializable

@Serializable
internal data class DependencyResolutionManagement(
    val repositories: List<@Serializable(with = ArtifactRepositoryTransformingSerializer::class) ArtifactRepository>? = null,
    val versionCatalogs: Set<VersionCatalog>? = null,
)
