package gradle.api.publish.maven

import gradle.api.provider.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.publish.maven.MavenPomDeveloper

/**
 * A developer of a Maven publication.
 *
 * @since 4.8
 * @see MavenPom
 *
 * @see MavenPomDeveloperSpec
 */
@Serializable
internal data class MavenPomDeveloper(
    /**
     * The unique ID of this developer in the SCM.
     */
    val id: String? = null,
    /**
     * The name of this developer.
     */
    val name: String? = null,
    /**
     * The email
     */
    val email: String? = null,
    /**
     * The URL of this developer.
     */
    val url: String? = null,
    /**
     * The organization name of this developer.
     */
    val organization: String? = null,
    /**
     * The organization's URL of this developer.
     */
    val organizationUrl: String? = null,
    /**
     * The roles of this developer.
     */
    val roles: Set<String>? = null,
    /**
     * The timezone of this developer.
     */
    val timezone: String? = null,
    /**
     * The properties of this developer.
     */
    val properties: Map<String, String>? = null,
) {

    fun applyTo(receiver: MavenPomDeveloper) {
        receiver.id tryAssign id
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
