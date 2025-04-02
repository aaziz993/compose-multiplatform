package klib.data.type.serialization.serializer

import klib.data.type.serialization.decodeAnyFromJsonElement
import klib.data.type.serialization.decodeAnyFromString
import klib.data.type.serialization.encodeAnyToJsonElement
import klib.data.type.serialization.encodeAnyToString
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder

public object AnySerializer : KSerializer<Any> {

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Any", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Any): Unit =
        (encoder as? JsonEncoder ?: error("Only JsonEncoder is supported")).json.let { json ->
            encoder.encodeJsonElement(json.encodeAnyToJsonElement(value))
        }

    override fun deserialize(decoder: Decoder): Any =
        (decoder as? JsonDecoder ?: error("Only JsonDecoder is supported"))
            .json.decodeAnyFromJsonElement(decoder.decodeJsonElement())!!
}

