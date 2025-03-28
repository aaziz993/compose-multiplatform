package gradle.plugins.project

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonTransformingSerializer

@Serializable
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

internal object ProjectLayoutTransformingSerializer :
    JsonTransformingSerializer<ProjectLayout>(ProjectLayout.serializer()) {

    override fun transformDeserialize(element: JsonElement): JsonElement =
        if (element is JsonPrimitive) JsonObject(
            mapOf("type" to element),
        )
        else element
}
