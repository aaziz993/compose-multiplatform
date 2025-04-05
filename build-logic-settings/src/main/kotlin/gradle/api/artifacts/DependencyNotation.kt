package gradle.api.artifacts

import klib.data.type.primitive.addPrefix
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
internal data class DependencyNotation(
    @SerialName("version")
    override val version: String? = null,
    @SerialName("group")
    private val _group: String? = null,
    @SerialName("name")
    private val _name: String? = null,
    @SerialName("module")
    private val _module: String? = null,
) : Notation() {

    @Transient
    val group = _group ?: _module?.substringBefore(":")

    @Transient
    val name = _name ?: _module?.substringAfter(":")

    override fun toString(): String = "$group:$name${version?.addPrefix(":")}"

    companion object {

        operator fun invoke(notation: String) =
            notation.split(":").let { notationParts ->
                DependencyNotation(
                    notationParts.getOrNull(2),
                    notationParts.first(),
                    notationParts[1],
                )
            }
    }
}
