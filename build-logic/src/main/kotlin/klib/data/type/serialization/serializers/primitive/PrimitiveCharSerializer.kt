package klib.data.type.serialization.serializers.primitive

import kotlinx.serialization.descriptors.PrimitiveKind

public abstract class PrimitiveCharSerializer<T : Any>(
    serialName: String,
    serializer: (T) -> Char,
    deserializer: (Char) -> T,
) : PrimitiveSerializer<T>(
    serialName,
    PrimitiveKind.CHAR,
    { encoder, value ->
        encoder.encodeChar(serializer(value))
    },
    {
        deserializer(it.decodeChar())
    },
)
