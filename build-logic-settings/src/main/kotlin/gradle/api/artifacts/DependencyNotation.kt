package gradle.api.artifacts

import klib.data.type.primitive.addPrefix
import klib.data.type.serialization.serializer.JsonObjectTransformingSerializer
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@KeepGeneratedSerializer
@Serializable(with = DependencyNotationObjectTransformingSerializer::class)
internal data class DependencyNotation(
    override val version: String? = null,
    @SerialName("group")
    private val _group: String? = null,
    @SerialName("name")
    private val _name: String? = null,
    @SerialName("module")
    private val _module: String? = null,
    @SerialName("notation")
    override val _notation: String? = null,
) : Notation() {

    @Transient
    val group = _group ?: _module!!.substringBefore(":")

    @Transient
    val name = _name ?: _module!!.substringAfter(":")

    @Transient
    val module = _module ?: "$_group:$_name"

    override fun toString(): String = _notation ?: "$module${version?.addPrefix(":")}"
}

private object DependencyNotationObjectTransformingSerializer :
    JsonObjectTransformingSerializer<DependencyNotation>(
        DependencyNotation.generatedSerializer(),
        "notation",
    )
