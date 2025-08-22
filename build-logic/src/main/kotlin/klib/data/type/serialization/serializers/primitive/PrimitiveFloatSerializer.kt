package klib.data.type.serialization.serializers.primitive

import kotlin.reflect.KClass
import kotlinx.serialization.descriptors.PrimitiveKind

public abstract class PrimitiveFloatSerializer<T : Any>(
    serialName: String,
    serializer: (T) -> Float,
    deserializer: (Float) -> T,
) : PrimitiveSerializer<T>(
    serialName,
    PrimitiveKind.FLOAT,
    { encoder, value ->
        encoder.encodeFloat(serializer(value))
    },
    {
        deserializer(it.decodeFloat())
    },
)
