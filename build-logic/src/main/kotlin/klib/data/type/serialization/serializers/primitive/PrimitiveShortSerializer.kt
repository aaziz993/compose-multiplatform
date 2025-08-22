package klib.data.type.serialization.serializers.primitive

import kotlin.reflect.KClass
import kotlinx.serialization.descriptors.PrimitiveKind

public abstract class PrimitiveShortSerializer<T : Any>(
    serialName: String,
    serializer: (T) -> Short,
    deserializer: (Short) -> T,
) : PrimitiveSerializer<T>(
    serialName,
    PrimitiveKind.SHORT,
    { encoder, value ->
        encoder.encodeShort(serializer(value))
    },
    {
        deserializer(it.decodeShort())
    },
)
