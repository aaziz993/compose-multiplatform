package klib.data.type.serialization.serializers.any

import klib.data.type.serialization.coders.any.asAnyDecoder
import klib.data.type.serialization.coders.any.asAnyTreeEncoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

public object NullableAnySerializer : KSerializer<Any?> {

    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("NullableAny", SerialKind.CONTEXTUAL)

    override fun serialize(encoder: Encoder, value: Any?): Unit = encoder.asAnyTreeEncoder.encodeNullableValue(value)

    @Suppress("UNCHECKED_CAST")
    override fun deserialize(decoder: Decoder): Any? = decoder.asAnyDecoder.decodeValue()
}

public typealias SerializableNullableAny = @Serializable(with = NullableAnySerializer::class) Any?

