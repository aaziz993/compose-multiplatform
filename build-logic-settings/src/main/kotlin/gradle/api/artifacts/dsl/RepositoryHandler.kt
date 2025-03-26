package gradle.api.artifacts.dsl

import gradle.api.repositories.ArtifactRepository
import gradle.api.repositories.ArtifactRepositoryKeyTransformingSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.initialization.Settings

@Serializable
@Suppress("JavaDefaultMethodsNotOverriddenByDelegation")
internal class RepositoryHandler: ArtifactRepositoryContainer by ArrayList<@Serializable(with = ArtifactRepositoryKeyTransformingSerializer::class) ArtifactRepository<out org.gradle.api.artifacts.repositories.ArtifactRepository>>() {

    context(Settings)
    fun applyTo(receiver: RepositoryHandler) =
        forEach { repository ->
            repository.applyTo(receiver)
        }

    context(Project)
    fun applyTo(receiver: RepositoryHandler) =
        forEach { repository ->
            repository.applyTo(receiver)
        }
}
