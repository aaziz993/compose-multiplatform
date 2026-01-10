package klib.data.type.serialization.serializers.primitive

import kotlinx.serialization.descriptors.PrimitiveKind

public abstract class IntSerializer<T : Any>(
    serialName: String,
    serializer: (T) -> Int,
    deserializer: (Int) -> T,
) : PrimitiveSerializer<T>(
    serialName,
    PrimitiveKind.INT,
    { encoder, value ->
        encoder.encodeInt(serializer(value))
    },
    {
        deserializer(it.decodeInt())
    },
)
