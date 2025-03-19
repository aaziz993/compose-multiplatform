package gradle.api.repositories.maven

import gradle.api.applyTo
import gradle.api.repositories.ArtifactRepository
import gradle.api.repositories.RepositoryContentDescriptorImpl
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.withType

@Serializable
@SerialName("gradlePluginPortal")
internal data class GradlePluginPortal(
    override val content: RepositoryContentDescriptorImpl? = null,
) : ArtifactRepository<org.gradle.api.artifacts.repositories.ArtifactRepository> {

    override val name: String
        get() = "gradlePluginPortal"

    context(Settings)
    override fun applyTo(recipient: RepositoryHandler) =
        applyTo(recipient.withType<org.gradle.api.artifacts.repositories.ArtifactRepository>()) { _, action ->
            recipient.gradlePluginPortal(action)
        }

    context(Project)
    override fun applyTo(recipient: RepositoryHandler) =
        applyTo(recipient.withType<org.gradle.api.artifacts.repositories.ArtifactRepository>()) { _, action ->
            recipient.gradlePluginPortal(action)
        }
}
