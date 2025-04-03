package gradle.api.publish.maven

import gradle.api.project.projectProperties
import gradle.accessors.settings
import klib.data.type.primitive.addSuffix
import gradle.api.provider.tryAssign
import gradle.reflection.tryApply
import klib.data.type.reflection.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPom
import org.gradle.kotlin.dsl.assign

/**
 * The POM for a Maven publication.
 *
 *
 * The [.withXml] method can be used to modify the
 * descriptor after it has been generated according to the publication data.
 * However, the preferred way to customize the project information to be published
 * is to use the dedicated properties exposed by this class, e.g.
 * [.getDescription]. Please refer to the official
 * [POM Reference](https://maven.apache.org/pom.html) for detailed
 * information about the individual properties.
 *
 * @since 1.4
 */
@Serializable
internal data class MavenPom(
    /**
     * Sets the packaging (for example: jar, war) for the publication represented by this POM.
     */
    val packaging: String? = null,
    /**
     * The name for the publication represented by this POM.
     *
     * @since 4.8
     */
    val name: String? = null,
    /**
     * A short, human-readable description for the publication represented by this POM.
     *
     * @since 4.8
     */
    val description: String? = null,
    /**
     * The URL of the home page for the project producing the publication represented by this POM.
     *
     * @since 4.8
     */
    val url: String? = null,
    /**
     * The year the project producing the publication represented by this POM was first created.
     *
     * @since 4.8
     */
    val inceptionYear: String? = null,
    /**
     * Configures the licenses for the publication represented by this POM.
     *
     * @since 4.8
     */
    val licenses: List<MavenPomLicense>? = null,
    /**
     * Configures the organization for the publication represented by this POM.
     *
     * @since 4.8
     */
    val organization: MavenPomOrganization? = null,
    /**
     * Configures the developers for the publication represented by this POM.
     *
     * @since 4.8
     */
    val developers: List<MavenPomDeveloper>? = null,
    /**
     * Configures the contributors for the publication represented by this POM.
     *
     * @since 4.8
     */
    val contributors: List<MavenPomContributor>? = null,
    /**
     * Configures the SCM (source control management) for the publication represented by this POM.
     *
     * @since 4.8
     */
    val scm: MavenPomScm? = null,
    /**
     * Configures the issue management for the publication represented by this POM.
     *
     * @since 4.8
     */
    val issueManagement: MavenPomIssueManagement? = null,
    /**
     * Configures the CI management for the publication represented by this POM.
     *
     * @since 4.8
     */
    val ciManagement: MavenPomCiManagement? = null,
    /**
     * Configures the distribution management for the publication represented by this POM.
     *
     * @since 4.8
     */
    val distributionManagement: MavenPomDistributionManagement? = null,
    /**
     * Configures the mailing lists for the publication represented by this POM.
     *
     * @since 4.8
     */
    val mailingLists: List<MavenPomMailingList>? = null,
    /**
     * Returns the properties for the publication represented by this POM.
     *
     * @since 5.3
     */
    val properties: Map<String, String>? = null,
) {

    context(Project)
    fun applyTo(receiver: MavenPom) {
        receiver::setPackaging trySet packaging
        receiver.name = name ?: project.name
        receiver.description = description ?: project.description
        receiver.url tryAssign (url
            ?: project.projectProperties.remote?.url
                ?.trimEnd('/')
                ?.addSuffix("/")
                ?.addSuffix(project.projectDir.toRelativeString(project.settings.settingsDir)))
        receiver.inceptionYear tryAssign (inceptionYear ?: project.projectProperties.year)

        (project.projectProperties.license?.let(::listOf).orEmpty() + licenses.orEmpty())
            .takeIf(List<*>::isNotEmpty)
            ?.let { licenses ->
                receiver.licenses {
                    licenses.forEach { license ->
                        license(license::applyTo)
                    }
                }
            }

        receiver::organization tryApply organization?.let { organization -> organization::applyTo }

        (project.projectProperties.developer?.let(::listOf).orEmpty() + developers.orEmpty())
            .takeIf(List<*>::isNotEmpty)
            ?.let { developers ->
                receiver.developers {
                    developers.forEach { developer ->
                        developer(developer::applyTo)
                    }
                }
            }

        contributors?.let { contributors ->
            receiver.contributors {
                contributors.forEach { contributor ->
                    contributor(contributor::applyTo)
                }
            }
        }

        (scm ?: project.projectProperties.remote)?.let { scm ->
            receiver.scm(scm::applyTo)
        }

        receiver::issueManagement tryApply issueManagement?.let { issueManagement -> issueManagement::applyTo }
        receiver::ciManagement tryApply ciManagement?.let { ciManagement -> ciManagement::applyTo }
        receiver::distributionManagement tryApply distributionManagement?.let { distributionManagement -> distributionManagement::applyTo }

        mailingLists?.let { mailingLists ->
            receiver.mailingLists {
                mailingLists.forEach { mailingList ->
                    mailingList(mailingList::applyTo)
                }
            }
        }

        receiver.properties tryAssign properties
    }
}
