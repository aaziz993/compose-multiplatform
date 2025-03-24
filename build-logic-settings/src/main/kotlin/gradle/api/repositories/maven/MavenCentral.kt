package gradle.api.repositories.maven

import gradle.api.applyTo
import gradle.api.repositories.PasswordCredentials
import gradle.api.repositories.RepositoryContentDescriptorImpl
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.artifacts.ArtifactRepositoryContainer
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.withType

@Serializable
@SerialName("mavenCentral")
internal data class MavenCentral(
    override val artifactUrls: Set<String>? = null,
    override val metadataSources: MavenArtifactRepository.MetadataSources? = null,
    override val mavenContent: MavenRepositoryContentDescriptor? = null,
    override val content: RepositoryContentDescriptorImpl? = null,
    override val allowInsecureProtocol: Boolean? = null,
    override val credentials: PasswordCredentials? = null,
) : MavenArtifactRepository {

    override val name: String
        get() = ArtifactRepositoryContainer.DEFAULT_MAVEN_CENTRAL_REPO_NAME

    override val url: String?
        get() = ArtifactRepositoryContainer.MAVEN_CENTRAL_URL

    context(settings: Settings)
    override fun applyTo(receiver: RepositoryHandler) =
        applyTo(receiver.withType<org.gradle.api.artifacts.repositories.MavenArtifactRepository>()) { _, action ->
            receiver.mavenCentral(action)
        }

    context(project: Project)
    override fun applyTo(receiver: RepositoryHandler) =
        applyTo(receiver.withType<org.gradle.api.artifacts.repositories.MavenArtifactRepository>()) { _, action ->
            receiver.mavenCentral(action)
        }
}
