package klib.data.type.serialization.serializer

import klib.data.type.serialization.decodeMapFromJsonElement
import klib.data.type.serialization.encodeAnyToJsonElement
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.elementNames
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

public open class JsonMapInheritedSerializer<T : MutableMap<String, V>, V>(private val tSerializer: KSerializer<T>) :
    KSerializer<T> {

    override val descriptor: SerialDescriptor = tSerializer.descriptor

    override fun serialize(encoder: Encoder, value: T) {
        (encoder as? JsonEncoder ?: error("Only JsonEncoder is supported")).json.let { json ->
            val unknownProperties = value - tSerializer.descriptor.elementNames

            encoder.encodeJsonElement(
                JsonObject(
                    json.encodeToJsonElement(tSerializer, value).jsonObject +
                        json.encodeAnyToJsonElement(unknownProperties).jsonObject,
                ),
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun deserialize(decoder: Decoder): T =
        (decoder as? JsonDecoder ?: error("Only JsonDecoder is supported"))
            .json.let { json ->

                val element = decoder.decodeJsonElement()

                val unknownProperties =
                    (json.decodeMapFromJsonElement<Any?>(element) - tSerializer.descriptor.elementNames) as Map<String, V>

                json.decodeFromJsonElement(tSerializer, element).apply {
                    putAll(unknownProperties)
                }
            }
}
