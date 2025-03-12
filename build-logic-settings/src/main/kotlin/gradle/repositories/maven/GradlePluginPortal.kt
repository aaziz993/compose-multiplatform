package gradle.repositories.maven

import gradle.repositories.ArtifactRepository
import gradle.repositories.RepositoryContentDescriptor
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.kotlin.dsl.withType

@Serializable
@SerialName("gradlePluginPortal")
internal data class GradlePluginPortal(
    override val content: RepositoryContentDescriptor? = null,
) : ArtifactRepository {

    override val name: String
        get() = "gradlePluginPortal"

    override fun applyTo(handler: RepositoryHandler) =
        super.applyTo(handler.withType<MavenArtifactRepository>()) {
            handler.gradlePluginPortal(it)
        }
}
