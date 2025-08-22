package klib.data.type.serialization.serializers.primitive

import kotlin.reflect.KClass
import kotlinx.serialization.descriptors.PrimitiveKind

public abstract class PrimitiveDoubleSerializer<T : Any>(
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
