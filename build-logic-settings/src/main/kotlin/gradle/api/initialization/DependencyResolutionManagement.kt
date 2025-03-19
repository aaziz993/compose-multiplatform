package gradle.api.initialization

import gradle.api.repositories.ArtifactRepository
import gradle.api.repositories.ArtifactRepositoryTransformingSerializer
import gradle.project.VersionCatalog
import java.util.SortedSet
import kotlinx.serialization.Serializable

@Serializable
internal data class DependencyResolutionManagement(
    val repositories: Set<@Serializable(with = ArtifactRepositoryTransformingSerializer::class) ArtifactRepository<*>>? = null,
    val versionCatalogs: SortedSet<VersionCatalog>? = null,
)
