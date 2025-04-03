package gradle.api.catalog

import klib.data.type.primitive.addPrefix
import klib.data.type.serialization.serializer.JsonObjectTransformingSerializer
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

@KeepGeneratedSerializer
@Serializable(with = PluginNotationObjectTransformingSerializer::class)
internal data class PluginNotation(
    val id: String,
    @SerialName("version")
    override val _version: @Serializable(with = VersionContentPolymorphicSerializer::class) Any? = null,
    val apply: Boolean = true,
) : VersionCatalogDependency() {

    @Transient
    override lateinit var versionCatalog: VersionCatalog

    override fun toString(): String = "$id:$id.gradle.plugin${version?.addPrefix(":")}"
}

private object PluginNotationObjectTransformingSerializer :
    JsonObjectTransformingSerializer<PluginNotation>(
        PluginNotation.generatedSerializer(),
        "id",
        "version",
    )

internal object PluginNotationContentPolymorphicSerializer :
    JsonContentPolymorphicSerializer<Any>(Any::class) {

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Any> =
        if (element is JsonPrimitive) String.serializer() else PluginNotation.serializer()
}
