package gradle.plugins.dependencycheck

import gradle.api.NamedObjectTransformingSerializer
import gradle.api.ProjectNamed
import gradle.api.trySet
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project

/**
 * Holder for the information regarding an additional CPE to be checked.
 */
@KeepGeneratedSerializer
@Serializable(with = AdditionalCpeObjectTransformingSerializer::class)
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

private object AdditionalCpeObjectTransformingSerializer
    : NamedObjectTransformingSerializer<AdditionalCpe>(AdditionalCpe.generatedSerializer())
