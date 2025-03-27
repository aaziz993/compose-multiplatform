package gradle.api.artifacts.dsl

import gradle.api.repositories.ArtifactRepository
import gradle.api.repositories.ArtifactRepositoryKeyTransformingSerializer
import kotlinx.serialization.Serializable

internal typealias ArtifactRepositoryContainer = List<@Serializable(with = ArtifactRepositoryKeyTransformingSerializer::class) ArtifactRepository<out org.gradle.api.artifacts.repositories.ArtifactRepository>>
