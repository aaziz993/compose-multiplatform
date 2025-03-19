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
@SerialName("mavenLocal")
internal data class MavenLocal(
    override val artifactUrls: Set<String>? = null,
    override val metadataSources: MavenArtifactRepository.MetadataSources? = null,
    override val mavenContent: MavenRepositoryContentDescriptor? = null,
    override val name: String = ArtifactRepositoryContainer.DEFAULT_MAVEN_LOCAL_REPO_NAME,
    override val content: RepositoryContentDescriptorImpl? = null,
    override val url: String? = null,
    override val allowInsecureProtocol: Boolean? = null,
    override val credentials: PasswordCredentials? = null,
) : MavenArtifactRepository {

    context(Settings)
    override fun applyTo(handler: RepositoryHandler) =
        applyTo(handler.withType<org.gradle.api.artifacts.repositories.MavenArtifactRepository>()) { _, action ->
            handler.mavenLocal(action)
        }

    context(Project)
    override fun applyTo(handler: RepositoryHandler) =
        applyTo(handler.withType<org.gradle.api.artifacts.repositories.MavenArtifactRepository>()) { _, action ->
            handler.mavenLocal(action)
        }
}
