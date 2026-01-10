package klib.data.type.serialization.serializers.primitive

import kotlinx.serialization.descriptors.PrimitiveKind

public abstract class LongSerializer<T : Any>(
    serialName: String,
    serializer: (T) -> Long,
    deserializer: (Long) -> T,
) : PrimitiveSerializer<T>(
    serialName,
    PrimitiveKind.LONG,
    { encoder, value ->
        encoder.encodeLong(serializer(value))
    },
    {
        deserializer(it.decodeLong())
    },
)
