package gradle.plugins.buildconfig

import gradle.api.NamedKeyTransformingSerializer
import gradle.api.ProjectNamed
import gradle.api.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class BuildConfigField(
    override val name: String? = null,
    val type: String? = null,
    val value: String? = null,
    val optional: Boolean? = null,
    val position: Int? = null,
) : ProjectNamed<com.github.gmazzo.gradle.plugins.BuildConfigField> {

    context(Project)
    override fun applyTo(receiver: com.github.gmazzo.gradle.plugins.BuildConfigField) {
        receiver.type tryAssign type
        receiver.value tryAssign value
        receiver.optional tryAssign optional
        receiver.position tryAssign position
    }
}

internal object BuildConfigFieldKeyTransformingSerializer
    : NamedKeyTransformingSerializer<BuildConfigField>(BuildConfigField.serializer())
