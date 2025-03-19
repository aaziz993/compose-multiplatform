package gradle.api.repositories.maven

import gradle.api.applyTo
import gradle.api.repositories.RepositoryContentDescriptorImpl
import gradle.api.repositories.PasswordCredentials
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.artifacts.ArtifactRepositoryContainer
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.withType

@Serializable
@SerialName("google")
internal data class Google(
    override val artifactUrls: Set<String>? = null,
    override val metadataSources: MavenArtifactRepository.MetadataSources? = null,
    override val mavenContent: MavenRepositoryContentDescriptor? = null,
    override val content: RepositoryContentDescriptorImpl? = null,
    override val allowInsecureProtocol: Boolean? = null,
    override val credentials: PasswordCredentials? = null,
) : MavenArtifactRepository {

    override val name: String
        get() = "google"

    override val url: String?
        get() = ArtifactRepositoryContainer.GOOGLE_URL

    context(Settings)
    override fun applyTo(handler: RepositoryHandler) =
        applyTo(handler.withType<org.gradle.api.artifacts.repositories.MavenArtifactRepository>()) { _, action ->
            handler.google(action)
        }

    context(Project)
    override fun applyTo(handler: RepositoryHandler) =
        applyTo(handler.withType<org.gradle.api.artifacts.repositories.MavenArtifactRepository>()) { _, action ->
            handler.google(action)
        }
}
