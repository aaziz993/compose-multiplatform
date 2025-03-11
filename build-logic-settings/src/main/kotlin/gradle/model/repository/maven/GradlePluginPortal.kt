package gradle.model.repository.maven

import gradle.model.repository.ArtifactRepository
import gradle.model.repository.RepositoryContentDescriptor
import gradle.publishing
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.withType

@Serializable
@SerialName("gradlePluginPortal")
internal data class GradlePluginPortal(
    override val content: RepositoryContentDescriptor? = null,
) : ArtifactRepository {

    override val name: String
        get() = "gradlePluginPortal"

    context(Project)
    override fun applyTo() =
        super.applyTo(
            publishing.repositories.withType<org.gradle.api.artifacts.repositories.MavenArtifactRepository>()){
            publishing.repositories.gradlePluginPortal(it)
        }
}
