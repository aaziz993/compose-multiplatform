package gradle.api.artifacts.dsl

import gradle.api.repositories.ArtifactRepository
import klib.data.type.serialization.serializer.ListSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.NothingSerializer
import org.gradle.api.Project
import org.gradle.api.initialization.Settings

@Suppress("JavaDefaultMethodsNotOverriddenByDelegation")
@Serializable(with = RepositoryHandlerListSerializer::class)
internal class RepositoryHandler(
    delegate: List<ArtifactRepository<out org.gradle.api.artifacts.repositories.ArtifactRepository>>
) : ArtifactRepositoryContainer,
    List<ArtifactRepository<out org.gradle.api.artifacts.repositories.ArtifactRepository>> by delegate {

    context(Settings)
    fun applyTo(receiver: org.gradle.api.artifacts.dsl.RepositoryHandler) =
        forEach { repository ->
            repository.applyTo(receiver)
        }

    context(Project)
    fun applyTo(receiver: org.gradle.api.artifacts.dsl.RepositoryHandler) =
        forEach { repository ->
            repository.applyTo(receiver)
        }
}

@Suppress("UNCHECKED_CAST")
private object RepositoryHandlerListSerializer :
    ListSerializer<
        ArtifactRepository<out org.gradle.api.artifacts.repositories.ArtifactRepository>,
        RepositoryHandler,
        RepositoryHandler,
        >(
        ArtifactRepository.serializer(NothingSerializer()) as KSerializer<ArtifactRepository<out org.gradle.api.artifacts.repositories.ArtifactRepository>>,
        ::RepositoryHandler,
    )
