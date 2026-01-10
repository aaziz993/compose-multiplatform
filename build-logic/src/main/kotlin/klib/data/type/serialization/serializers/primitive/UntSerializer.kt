package klib.data.type.serialization.serializers.primitive

import kotlinx.serialization.descriptors.PrimitiveKind

public abstract class UntSerializer<T : Any>(
    serialName: String,
    serializer: (T) -> UInt,
    deserializer: (UInt) -> T,
) : PrimitiveSerializer<T>(
    serialName,
    PrimitiveKind.INT,
    { encoder, value ->
        encoder.encodeInt(serializer(value).toInt())
    },
    {
        deserializer(it.decodeInt().toUInt())
    },
)
