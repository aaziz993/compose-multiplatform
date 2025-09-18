package klib.data.type.serialization.serializers.primitive

import kotlin.reflect.KClass
import kotlinx.serialization.descriptors.PrimitiveKind

public abstract class PrimitiveByteSerializer<T : Any>(
    serialName: String,
    serializer: (T) -> Byte,
    deserializer: (Byte) -> T,
) : PrimitiveSerializer<T>(
    serialName,
    PrimitiveKind.BYTE,
    { encoder, value ->
        encoder.encodeByte(serializer(value))
    },
    {
        deserializer(it.decodeByte())
    },
)
