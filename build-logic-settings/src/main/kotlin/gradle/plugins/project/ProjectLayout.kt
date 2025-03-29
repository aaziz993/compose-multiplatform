package gradle.plugins.project

import gradle.serialization.serializer.JsonObjectTransformingContentPolymorphicSerializer
import kotlinx.serialization.Serializable

@Serializable(with = ProjectLayoutValueTransformingContentPolymorphicSerializer::class)
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

private object ProjectLayoutValueTransformingContentPolymorphicSerializer
    : JsonObjectTransformingContentPolymorphicSerializer<ProjectLayout>(
    ProjectLayout::class,
)



