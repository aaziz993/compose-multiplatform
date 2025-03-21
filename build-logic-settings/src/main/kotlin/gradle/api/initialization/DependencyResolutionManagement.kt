package gradle.api.initialization

import gradle.api.repositories.ArtifactRepository
import gradle.api.repositories.ArtifactRepositoryTransformingSerializer
import gradle.project.VersionCatalog

import kotlinx.serialization.Serializable

@Serializable
internal data class DependencyResolutionManagement(
    val repositories: Set<@Serializable(with = ArtifactRepositoryTransformingSerializer::class) ArtifactRepository<*>>? = null,
    val versionCatalogs: LinkedHashSet<VersionCatalog>? = null,
)
