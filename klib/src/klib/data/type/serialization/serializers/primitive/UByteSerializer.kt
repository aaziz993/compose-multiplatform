package klib.data.type.serialization.serializers.primitive

import kotlinx.serialization.descriptors.PrimitiveKind

public abstract class UByteSerializer<T : Any>(
    serialName: String,
    serializer: (T) -> UByte,
    deserializer: (UByte) -> T,
) : PrimitiveSerializer<T>(
    serialName,
    PrimitiveKind.BYTE,
    { encoder, value ->
        encoder.encodeByte(serializer(value).toByte())
    },
    {
        deserializer(it.decodeByte().toUByte())
    },
)
