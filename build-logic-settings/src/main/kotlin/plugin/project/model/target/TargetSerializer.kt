package plugin.project.model.target

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.serializer
import net.pearx.kasechange.toScreamingSnakeCase

internal object TargetSerializer : KSerializer<Target> {

    override val descriptor: SerialDescriptor
        get() = buildClassSerialDescriptor("Target") {
            element<TargetType>("type")
            element<String?>("name")
        }

    override fun serialize(encoder: Encoder, value: Target) {
        encoder.encodeStructure(descriptor) {
            encodeSerializableElement(descriptor, 0, TargetType::class.serializer(), value.type)
            encodeNullableSerializableElement(descriptor, 1, String::class.serializer(), value.targetName)
        }
    }

    override fun deserialize(decoder: Decoder): Target {
        val element = decoder.decodeSerializableValue(JsonElement.serializer())
        return when (element) {
            is JsonObject -> {
                Target(TargetType.valueOf(element["type"]!!.jsonPrimitive.content.toScreamingSnakeCase()), element["targetName"]!!.jsonPrimitive.content)
            }

            else -> Target(TargetType.valueOf(element.jsonPrimitive.content.toScreamingSnakeCase()))
        }
    }
}
