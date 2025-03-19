package gradle.api.repositories.maven

import gradle.api.repositories.RepositoryContentDescriptorImpl
import gradle.api.repositories.PasswordCredentials
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("jetbrainsCompose")
internal data class JetbrainsCompose(
    override val artifactUrls: Set<String>? = null,
    override val metadataSources: MavenArtifactRepository.MetadataSources? = null,
    override val mavenContent: MavenRepositoryContentDescriptor? = null,
    override val content: RepositoryContentDescriptorImpl? = null,
    override val allowInsecureProtocol: Boolean? = null,
    override val credentials: PasswordCredentials? = null,
) : MavenArtifactRepository {

    override val name: String
        get() = "jetbrainsCompose"

    override val url: String?
        get() = "https://maven.pkg.jetbrains.space/public/p/compose/dev"
}
