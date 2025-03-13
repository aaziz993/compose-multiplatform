package gradle.project

import gradle.api.repositories.ArtifactRepository
import gradle.api.repositories.ArtifactRepositoryTransformingSerializer
import kotlinx.serialization.Serializable

@Serializable
internal data class DependencyResolutionManagement(
    val repositories: List<@Serializable(with = ArtifactRepositoryTransformingSerializer::class) ArtifactRepository>? = null,
    val versionCatalogs: Set<VersionCatalog>? = null,
)
