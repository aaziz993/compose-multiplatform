package gradle.repositories.maven

import gradle.repositories.RepositoryContentDescriptor
import gradle.repositories.RepositoryPasswordCredentials
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("jetbrainsCompose")
internal data class JetbrainsCompose(
    override val artifactUrls: Set<String>? = null,
    override val metadataSources: MavenArtifactRepository.MetadataSources? = null,
    override val mavenContent: MavenRepositoryContentDescriptor? = null,
    override val content: RepositoryContentDescriptor? = null,
    override val allowInsecureProtocol: Boolean? = null,
    override val credentials: RepositoryPasswordCredentials? = null,
) : MavenArtifactRepository {

    override val name: String
        get() = "jetbrainsCompose"

    override val url: String?
        get() = "https://maven.pkg.jetbrains.space/public/p/compose/dev"
}
