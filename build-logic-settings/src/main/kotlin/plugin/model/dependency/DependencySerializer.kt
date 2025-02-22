package plugin.model.dependency

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.serializer

internal object DependencySerializer : KSerializer<Dependency> {

    override val descriptor: SerialDescriptor
        get() = buildClassSerialDescriptor("DependencyNotation") {
            element<String>("notation")
            element<String?>("configuration")
        }

    override fun serialize(encoder: Encoder, value: Dependency) =
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.notation)
            encodeStringElement(descriptor, 1, value.configuration)
        }

    override fun deserialize(decoder: Decoder): Dependency {
        val element = decoder.decodeSerializableValue(JsonElement.serializer())
        return when (element) {
            is JsonObject -> Dependency(element.keys.single(), element.values.single().jsonPrimitive.content)

            else -> Dependency(element.jsonPrimitive.content)
        }
    }
}
