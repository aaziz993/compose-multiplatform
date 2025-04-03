package gradle.api.catalog

import klib.data.type.primitive.addPrefix
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

@Serializable
internal data class Notation(
    @SerialName("group")
    private val _group: String? = null,
    @SerialName("name")
    private val _name: String? = null,
    @SerialName("module")
    private val _module: String? = null,
    @SerialName("version")
    override val _version: @Serializable(with = VersionContentPolymorphicSerializer::class) Any? = null,
) : VersionCatalogDependency() {

    @Transient
    override lateinit var versionCatalog: VersionCatalog

    val group by lazy {
        _group ?: _module!!.substringBefore(":")
    }

    val name by lazy {
        _name ?: _module!!.substringAfter(":")
    }

    val module by lazy {
        _module ?: "$_group:$_name"
    }

    override fun toString(): String = "$module${version?.addPrefix(":")}"
}

internal object NotationContentPolymorphicSerializer :
    JsonContentPolymorphicSerializer<Any>(Any::class) {

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Any> =
        if (element is JsonPrimitive) String.serializer() else Notation.serializer()
}
