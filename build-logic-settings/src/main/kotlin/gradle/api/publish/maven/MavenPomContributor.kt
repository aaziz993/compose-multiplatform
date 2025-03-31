package gradle.api.publish.maven

import gradle.api.provider.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.publish.maven.MavenPomContributor

/**
 * A contributor of a Maven publication.
 *
 * @since 4.8
 * @see MavenPom
 *
 * @see MavenPomContributorSpec
 */
@Serializable
internal data class MavenPomContributor(
    /**
     * The name of this contributor.
     */
    val name: String? = null,
    /**
     * The email
     */
    val email: String? = null,
    /**
     * The URL of this contributor.
     */
    val url: String? = null,
    /**
     * The organization name of this contributor.
     */
    val organization: String? = null,
    /**
     * The organization's URL of this contributor.
     */
    val organizationUrl: String? = null,
    /**
     * The roles of this contributor.
     */
    val roles: Set<String>? = null,
    /**
     * The timezone of this contributor.
     */
    val timezone: String? = null,
    /**
     * The properties of this contributor.
     */
    val properties: Map<String?, String>? = null,
) {

    fun applyTo(receiver: MavenPomContributor) {
        receiver.name tryAssign name
        receiver.email tryAssign email
        receiver.url tryAssign url
        receiver.organization tryAssign organization
        receiver.organizationUrl tryAssign organizationUrl
        receiver.roles tryAssign roles
        receiver.timezone tryAssign timezone
        receiver.properties tryAssign properties
    }
}
