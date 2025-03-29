package gradle.serialization.serializer

import gradle.serialization.decodeAnyFromString
import gradle.serialization.encodeAnyToString
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder

public object OptionalAnySerializer : KSerializer<Any?> {

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Any", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Any?) {
        (encoder as? JsonEncoder ?: error("Only JsonEncoder is supported")).json.encodeAnyToString(value)
    }

    override fun deserialize(decoder: Decoder): Any? =
        (decoder as? JsonDecoder ?: error("Only JsonDecoder is supported"))
            .json.decodeAnyFromString(decoder.decodeString())
}
