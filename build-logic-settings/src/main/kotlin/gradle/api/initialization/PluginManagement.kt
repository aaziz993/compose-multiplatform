package gradle.api.initialization

import gradle.api.repositories.ArtifactRepository
import gradle.api.repositories.ArtifactRepositoryTransformingSerializer
import java.util.SortedSet
import kotlinx.serialization.Serializable

@Serializable
internal data class PluginManagement(
    val repositories: Set<@Serializable(with = ArtifactRepositoryTransformingSerializer::class) ArtifactRepository<*>>? = null,
)
