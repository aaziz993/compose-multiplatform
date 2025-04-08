package klib.data.type.serialization.serializer

import klib.data.type.serialization.encoder.TreeEncoder
import klib.data.type.serialization.encoder.asTreeDecoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

public sealed class GenericAnySerializer<T> : KSerializer<T> {

    override fun serialize(encoder: Encoder, value: T): Unit =
        TreeEncoder(encoder).encodeAny(value)

    @Suppress("UNCHECKED_CAST")
    override fun deserialize(decoder: Decoder): T = decoder.asTreeDecoder.decodeAny() as T
}