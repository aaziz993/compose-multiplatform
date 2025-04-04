package gradle.api.artifacts

import klib.data.type.primitive.addPrefix
import klib.data.type.serialization.serializer.JsonObjectTransformingSerializer
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@KeepGeneratedSerializer
@Serializable(with = PluginNotationObjectTransformingSerializer::class)


internal data class PluginNotation(
    override val version: String? = null,
    @SerialName("notation")
    override val _notation: String? = null,
    val id: String,
    val apply: Boolean = true,
) : Notation() {

    override fun toString(): String = _notation ?: "$id:$id.gradle.plugin${version?.addPrefix(":")}"
}

private object PluginNotationObjectTransformingSerializer :
    JsonObjectTransformingSerializer<PluginNotation>(
        PluginNotation.generatedSerializer(),
        "notation",
    )
