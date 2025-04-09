package klib.data.type.serialization.serializer

import klib.data.type.serialization.encoder.decodeAny
import klib.data.type.serialization.encoder.encodeAny
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


public object AnySerializer : KSerializer<Any> {

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Any", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Any): Unit =
        encoder.encodeAny(value)

    @Suppress("UNCHECKED_CAST")
    override fun deserialize(decoder: Decoder): Any = decoder.decodeAny()!!
}