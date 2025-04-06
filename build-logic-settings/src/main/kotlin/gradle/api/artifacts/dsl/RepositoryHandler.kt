package gradle.api.artifacts.dsl

import gradle.api.repositories.ArtifactRepository
import klib.data.type.serialization.serializer.ListSerializer
import klib.data.type.serialization.serializer.SetSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.NothingSerializer
import org.gradle.api.Project
import org.gradle.api.initialization.Settings

@Suppress("JavaDefaultMethodsNotOverriddenByDelegation")
@Serializable(with = RepositoryHandlerListSerializer::class)
internal abstract class RepositoryHandler : ArtifactRepositoryContainer {

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

@Serializable
private class MutableRepositoryHandler
    : RepositoryHandler(), MutableList<ArtifactRepository<out org.gradle.api.artifacts.repositories.ArtifactRepository>> by mutableListOf()

@Suppress("UNCHECKED_CAST")
private object RepositoryHandlerListSerializer :
    KSerializer<RepositoryHandler> by ListSerializer(
        ArtifactRepository.serializer(NothingSerializer()) as KSerializer<ArtifactRepository<out org.gradle.api.artifacts.repositories.ArtifactRepository>>,
        ::MutableRepositoryHandler,
    )
