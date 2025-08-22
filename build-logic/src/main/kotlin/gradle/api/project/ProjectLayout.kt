package gradle.api.project

import klib.data.type.serialization.serializers.transform.ReflectionMapTransformingPolymorphicSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(with = ReflectionProjectLayoutMapTransformingPolymorphicSerializer::class)
public sealed class ProjectLayout {

    @Serializable
    @SerialName("default")
    public object Default : ProjectLayout()

    @Serializable
    @SerialName("flat")
    public data class Flat(
        val targetDelimiter: String = "@",
        val androidAllVariantsDelimiter: String = "+",
        val androidVariantDelimiter: String = ""
    ) : ProjectLayout()
}

private object ReflectionProjectLayoutMapTransformingPolymorphicSerializer
    : ReflectionMapTransformingPolymorphicSerializer<ProjectLayout>(
    ProjectLayout::class,
)



