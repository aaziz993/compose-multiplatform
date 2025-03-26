package gradle.plugins.dependencycheck

import gradle.api.NamedKeyTransformingSerializer
import gradle.api.ProjectNamed
import gradle.plugins.android.BuildType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project

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
) : ProjectNamed<org.owasp.dependencycheck.gradle.extension.AdditionalCpe> {

    context(Project)
    override fun applyTo(receiver: org.owasp.dependencycheck.gradle.extension.AdditionalCpe) {
        description?.let(receiver::setDescription)
        cpe?.let(receiver::setCpe)
    }
}

internal object AdditionalCpeKeyTransformingSerializer
    : NamedKeyTransformingSerializer<AdditionalCpe>(AdditionalCpe.serializer())
