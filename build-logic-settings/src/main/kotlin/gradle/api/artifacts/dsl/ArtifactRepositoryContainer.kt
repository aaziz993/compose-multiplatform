package gradle.api.artifacts.dsl

import gradle.api.publish.maven.ArtifactTransformingSerializer
import gradle.api.repositories.ArtifactRepository
import kotlinx.serialization.Serializable

internal typealias ArtifactRepositoryContainer = List<@Serializable(with = ArtifactKeyTransformingSerializer::class) ArtifactRepository<out org.gradle.api.artifacts.repositories.ArtifactRepository>>
