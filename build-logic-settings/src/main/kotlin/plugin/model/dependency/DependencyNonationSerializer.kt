package plugin.model.dependency

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.serializer

internal object DependencyNonationSerializer : KSerializer<DependencyNotation> {

    override val descriptor: SerialDescriptor
        get() = String::class.serializer().descriptor

    override fun serialize(encoder: Encoder, value: DependencyNotation) {
        encoder.encodeString("${value.notation}:")
    }

    override fun deserialize(decoder: Decoder): DependencyNotation {
        val element = decoder.decodeSerializableValue(JsonElement.serializer())
        return when (element) {
            is JsonObject -> {
                val notation = element.keys.single()
                if (element.values.size == 1) {
                    when (element.values.single().jsonPrimitive.content) {
                        "compile-only" -> return DependencyNotation(notation, compile = true, runtime = false)
                        "runtime-only" -> return DependencyNotation(notation, compile = false, runtime = true)
                        "exported" -> return DependencyNotation(notation, exported = true)
                    }
                }
                else {
                    val exported = element.jsonObject["exported"]?.jsonPrimitive?.boolean == true

                    when (element.jsonObject["scope"]?.jsonPrimitive?.content) {
                        "compile-only" -> return DependencyNotation(notation, compile = true, runtime = false, exported = exported)
                        "runtime-only" -> return DependencyNotation(notation, compile = false, runtime = true, exported = exported)
                    }
                }
                DependencyNotation(notation)
            }

            else -> DependencyNotation(element.jsonPrimitive.content)
        }
    }
}
