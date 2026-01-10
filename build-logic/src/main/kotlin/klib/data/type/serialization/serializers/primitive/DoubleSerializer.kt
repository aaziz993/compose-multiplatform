package klib.data.type.serialization.serializers.primitive

import kotlinx.serialization.descriptors.PrimitiveKind

public abstract class DoubleSerializer<T : Any>(
    serialName: String,
    serializer: (T) -> Double,
    deserializer: (Double) -> T,
) : PrimitiveSerializer<T>(
    serialName,
    PrimitiveKind.DOUBLE,
    { encoder, value ->
        encoder.encodeDouble(serializer(value))
    },
    {
        deserializer(it.decodeDouble())
    },
)
