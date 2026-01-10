package klib.data.type.serialization.serializers.primitive

import kotlinx.serialization.descriptors.PrimitiveKind

public abstract class BooleanSerializer<T : Any>(
    serialName: String,
    serializer: (T) -> Boolean,
    deserializer: (Boolean) -> T,
) : PrimitiveSerializer<T>(
    serialName,
    PrimitiveKind.BOOLEAN,
    { encoder, value ->
        encoder.encodeBoolean(serializer(value))
    },
    {
        deserializer(it.decodeBoolean())
    },
)
