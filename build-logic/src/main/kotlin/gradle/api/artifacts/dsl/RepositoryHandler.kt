package gradle.api.artifacts.dsl

import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.jetbrains.compose.jetbrainsCompose

// Compose repository
public fun RepositoryHandler.jetbrainsCompose(): MavenArtifactRepository = jetbrainsCompose()
