package gradle.plugins.dependencycheck

import gradle.api.ProjectNamed
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.owasp.dependencycheck.gradle.extension.AdditionalCpe

/**
 * Holder for the information regarding an additional CPE to be checked.
 */
@Serializable
internal data class AdditionalCpe(
    override val name: String? = null,
    /**
     * Description for the what the CPE represents.
     */
    val description: String? = null,
    /**
     * The CPE to be checked against the database.
     */
    val cpe: String? = null,
) : ProjectNamed<AdditionalCpe> {

    context(Project)
    override fun applyTo(receiver: AdditionalCpe) {
        description?.let(receiver::setDescription)
        cpe?.let(receiver::setCpe)
    }
}
