package plugin.model.dependency

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.serializer

internal object DependencySerializer : KSerializer<Dependency> {

    override val descriptor: SerialDescriptor
        get() = String::class.serializer().descriptor

    override fun serialize(encoder: Encoder, value: Dependency) {
        encoder.encodeString("${value.notation}:")
    }

    override fun deserialize(decoder: Decoder): Dependency {
        val element = decoder.decodeSerializableValue(JsonElement.serializer())
        println("DESERIALIZED: " + element)
        return when (element) {
            is JsonObject -> {
                val configuration = element.values.single()
                Dependency(element.keys.single())
            }

            else -> Dependency(decoder.decodeString())
        }
    }
}
