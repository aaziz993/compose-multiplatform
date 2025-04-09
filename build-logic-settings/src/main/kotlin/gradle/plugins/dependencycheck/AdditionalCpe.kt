package gradle.plugins.dependencycheck

import gradle.api.NamedMapTransformingSerializer
import gradle.api.ProjectNamed
import klib.data.type.reflection.trySet
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project

/**
 * Holder for the information regarding an additional CPE to be checked.
 */
@KeepGeneratedSerializer
@Serializable(with = AdditionalCpeMapTransformingSerializer::class)
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
        receiver::setDescription trySet description
        receiver::setCpe trySet cpe
    }
}

private object AdditionalCpeMapTransformingSerializer
    : NamedMapTransformingSerializer<AdditionalCpe>(AdditionalCpe.generatedSerializer())
