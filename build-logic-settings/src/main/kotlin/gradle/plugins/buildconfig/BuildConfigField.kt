package gradle.plugins.buildconfig

import gradle.api.NamedKeyValueTransformingSerializer
import gradle.api.ProjectNamed
import gradle.api.tryAssign
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@OptIn(ExperimentalSerializationApi::class)
@KeepGeneratedSerializer
@Serializable(with = BuildConfigFieldKeyValueTransformingSerializer::class)
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

private object BuildConfigFieldKeyValueTransformingSerializer
    : NamedKeyValueTransformingSerializer<BuildConfigField>(BuildConfigField.generatedSerializer())
