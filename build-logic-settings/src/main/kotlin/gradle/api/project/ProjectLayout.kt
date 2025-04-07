package gradle.api.project

import klib.data.type.serialization.json.serializer.ReflectionJsonObjectTransformingPolymorphicSerializer
import kotlinx.serialization.Serializable

@Serializable(with = ReflectionProjectLayoutObjectTransformingPolymorphicSerializer::class)
internal sealed class ProjectLayout {

    @Serializable
    internal object Default : ProjectLayout()

    @Serializable
    internal data class Flat(
        val targetDelimiter: String = "@",
        val androidAllVariantsDelimiter: String = "+",
        val androidVariantDelimiter: String = ""
    ) : ProjectLayout()
}

private object ReflectionProjectLayoutObjectTransformingPolymorphicSerializer
    : ReflectionJsonObjectTransformingPolymorphicSerializer<ProjectLayout>(
    ProjectLayout::class,
)



