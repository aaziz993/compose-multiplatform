package gradle.model.project

import gradle.model.repository.ArtifactRepository
import gradle.model.repository.ArtifactRepositoryTransformingSerializer
import kotlinx.serialization.Serializable

@Serializable
internal data class DependencyResolutionManagement(
    val repositories: List<@Serializable(with = ArtifactRepositoryTransformingSerializer::class) ArtifactRepository>? = null,
    val versionCatalogs: Set<VersionCatalog>? = null,
)
