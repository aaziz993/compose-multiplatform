package klib.data.type.serialization.json.serializer

import klib.data.type.serialization.json.decodeAnyFromJsonElement
import klib.data.type.serialization.json.encodeAnyToJsonElement
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder

public abstract class JsonGenericAnySerializer<T> : KSerializer<T> {

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Any", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: T): Unit =
        (encoder as? JsonEncoder ?: error("Only JsonEncoder is supported")).json.let { json ->
            encoder.encodeJsonElement(json.encodeAnyToJsonElement(value))
        }

    @Suppress("UNCHECKED_CAST")
    override fun deserialize(decoder: Decoder): T =
        (decoder as? JsonDecoder ?: error("Only JsonDecoder is supported"))
            .json.decodeAnyFromJsonElement(decoder.decodeJsonElement()) as T
}
