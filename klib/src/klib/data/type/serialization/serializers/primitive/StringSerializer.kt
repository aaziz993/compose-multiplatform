package klib.data.type.serialization.serializers.primitive

import kotlinx.serialization.descriptors.PrimitiveKind

public abstract class StringSerializer<T : Any>(
    serialName: String,
    serializer: (T) -> String = { value -> value.toString() },
    deserializer: (String) -> T,
) : PrimitiveSerializer<T>(
        serialName,
        PrimitiveKind.STRING,
        { encoder, value ->
            encoder.encodeString(serializer(value))
        },
        {
            deserializer(it.decodeString())
        },
)
