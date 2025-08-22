package gradle.api.artifacts.dsl

import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.kotlin.dsl.maven

// Compose repository
public fun RepositoryHandler.jetbrainsCompose(): MavenArtifactRepository =
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev") {
        name = "jetbrainsCompose"
    }
