package klib.data.type.serialization.serializers.primitive

import kotlinx.serialization.descriptors.PrimitiveKind

public abstract class ULongSerializer<T : Any>(
    serialName: String,
    serializer: (T) -> ULong,
    deserializer: (ULong) -> T,
) : PrimitiveSerializer<T>(
    serialName,
    PrimitiveKind.LONG,
    { encoder, value ->
        encoder.encodeLong(serializer(value).toLong())
    },
    {
        deserializer(it.decodeLong().toULong())
    },
)
