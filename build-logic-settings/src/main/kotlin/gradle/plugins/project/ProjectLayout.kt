package gradle.plugins.project

import gradle.serialization.serializer.JsonContentPolymorphicSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonTransformingSerializer

@Serializable(with = ProjectLayoutTransformingSerializer::class)
internal sealed class ProjectLayout {

    @Serializable
    internal object Default : ProjectLayout()

    @Serializable
    internal data class Flat(
        val targetDelimiter: String = "@",
        val androidVariantsDelimiter: String = "+",
        val androidVariantDelimiter: String = ""
    ) : ProjectLayout()
}

private object ProjectLayoutContentPolymorphicSerializer : JsonContentPolymorphicSerializer<ProjectLayout>(
    ProjectLayout::class,
)

private object ProjectLayoutTransformingSerializer :
    JsonTransformingSerializer<ProjectLayout>(ProjectLayoutContentPolymorphicSerializer) {

    override fun transformDeserialize(element: JsonElement): JsonElement =
        if (element is JsonPrimitive) JsonObject(
            mapOf("type" to element),
        )
        else element
}
