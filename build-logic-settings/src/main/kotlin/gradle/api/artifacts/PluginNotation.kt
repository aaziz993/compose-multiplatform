package gradle.api.artifacts

import klib.data.type.primitive.addPrefix
import kotlinx.serialization.Serializable

@Serializable
internal data class PluginNotation(
    override val version: String? = null,
    val id: String,
    val apply: Boolean = true,
) : Notation() {

    override fun toString(): String = "$id:$id.gradle.plugin${version?.addPrefix(":")}"

    companion object {

        operator fun invoke(notation: String) =
            notation.split(":").let { notationParts ->
                PluginNotation(notationParts[1], notationParts.first())
            }
    }
}
