package gradle.api.artifacts.dsl

import gradle.api.repositories.ArtifactRepository

internal typealias ArtifactRepositoryContainer = List<ArtifactRepository<out org.gradle.api.artifacts.repositories.ArtifactRepository>>
