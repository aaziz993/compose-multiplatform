package gradle.model.gradle.publish.repository.maven

import gradle.model.gradle.publish.repository.ArtifactRepository
import gradle.model.gradle.publish.repository.RepositoryContentDescriptor
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.artifacts.dsl.RepositoryHandler

@Serializable
@SerialName("gradlePluginPortal")
internal data class GradlePluginPortal(
    override val name: String? = null,
    override val content: RepositoryContentDescriptor? = null,
) : ArtifactRepository {

    override fun applyTo(handler: RepositoryHandler) {
        handler.gradlePluginPortal(::applyTo)
    }
}
