package klib.data.type.serialization.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.elementNames
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

public open class JsonUnknownPreservingSerializer<T : MutableMap<String, V>, V>(
    private val tSerializer: KSerializer<T>,
    private val vSerializer: KSerializer<V>
) : KSerializer<T> {

    override val descriptor: SerialDescriptor = tSerializer.descriptor

    override fun serialize(encoder: Encoder, value: T) {
        (encoder as? JsonEncoder ?: error("Only JsonEncoder is supported")).json.let { json ->
            val unknownProperties = (value - tSerializer.descriptor.elementNames)
                .mapValues { (_, value) -> json.encodeToJsonElement(vSerializer, value) }

            encoder.encodeJsonElement(
                JsonObject(
                    json.encodeToJsonElement(tSerializer, value).jsonObject +
                        unknownProperties,
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
                    (element.jsonObject - tSerializer.descriptor.elementNames)
                        .mapValues { (_, value) -> json.decodeFromJsonElement(vSerializer, value) }

                json.decodeFromJsonElement(tSerializer, element).apply {
                    putAll(unknownProperties)
                }
            }
}
