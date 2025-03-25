package gradle.api.initialization

import gradle.api.repositories.ArtifactRepository
import gradle.api.repositories.ArtifactRepositoryTransformingSerializer

import kotlinx.serialization.Serializable

@Serializable
internal data class PluginManagement(
    val repositories: LinkedHashSet<@Serializable(with = ArtifactRepositoryTransformingSerializer::class) ArtifactRepository<*>>? = null,
)
