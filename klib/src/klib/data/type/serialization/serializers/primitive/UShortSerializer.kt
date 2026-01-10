package klib.data.type.serialization.serializers.primitive

import kotlinx.serialization.descriptors.PrimitiveKind

public abstract class UShortSerializer<T : Any>(
    serialName: String,
    serializer: (T) -> UShort,
    deserializer: (UShort) -> T,
) : PrimitiveSerializer<T>(
    serialName,
    PrimitiveKind.SHORT,
    { encoder, value ->
        encoder.encodeShort(serializer(value).toShort())
    },
    {
        deserializer(it.decodeShort().toUShort())
    },
)
